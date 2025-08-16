package ru.yandex.practicum.starter;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.aggregation.AggregationEventSnapshotImpl;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter implements CommandLineRunner {
    private final Consumer<String, SpecificRecordBase> consumer;
    private final Producer<String, SpecificRecordBase> producer;
    private final AggregationEventSnapshotImpl aggregationSnapshot;
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    private volatile boolean running = true;

    @Value("${aggregator.kafka.topic.telemetry-sensors}")
    private String sensorsTopic;

    @Value("${aggregator.kafka.topic.telemetry-snapshots}")
    private String snapshotsTopic;


    @Override
    public void run(String... args) {
        try {
            consumer.subscribe(List.of(sensorsTopic));
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

            while (running) {
                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(Duration.ofMillis(1000));
                int count = 0;
                for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                    log.info("Обрабатываем очередное сообщение {}", record.value());
                    handleRecord(record);
                    manageOffsets(record, count);
                    count++;
                }
                consumer.commitAsync();
            }
        } catch (WakeupException ignores) {
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            try {
                producer.flush();
                consumer.commitSync();
            } finally {
                log.info("Закрываем CONSUMER");
                consumer.close();
                log.info("Закрываем PRODUCER");
                producer.close();
            }
        }
    }

    @PreDestroy
    public void shutdown() {
        consumer.wakeup();
        running = false;
    }

    private void handleRecord(ConsumerRecord<String, SpecificRecordBase> record) {
        log.info("топик = {}, партиция = {}, смещение = {}, значение: {}",
                record.topic(), record.partition(), record.offset(), record.value());
        SensorEventAvro event = (SensorEventAvro) record.value();
        Optional<SensorsSnapshotAvro> snapshot = aggregationSnapshot.updateState(event);
        log.info("Получили снимок состояния {}", snapshot);
        if (snapshot.isPresent()) {
            log.info("Запись в топик Kafka");
            ProducerRecord<String, SpecificRecordBase> producerRecord = new ProducerRecord<>(snapshotsTopic,
                    null, event.getTimestamp().toEpochMilli(), event.getHubId(), snapshot.get());

            producer.send(producerRecord);
            log.info("SNAPSHOT обновлен и отправлен {}", snapshot);
        } else {
            log.info("SNAPSHOT не обновлен");
        }
    }

    private void manageOffsets(ConsumerRecord<String, SpecificRecordBase> record, int count) {
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );

        if (count % 10 == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if (exception != null) {
                    log.warn("Ошибка во время фиксации оффсетов: {}", offsets, exception);
                }
            });
        }
    }
}