package ru.yandex.practicum.kafka;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.service.snapshot.SnapshotHandler;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotProcessor {

    private final Consumer<String, SensorsSnapshotAvro> consumer;
    private final SnapshotHandler snapshotHandler;
    private volatile boolean isRunning = true;

    @Value("${analyzer.kafka.topic.telemetry.snapshots}")
    private String topic;

    public void start() {
        consumer.subscribe(List.of(topic));
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

        try {
            while (isRunning) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = consumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                    handleRecord(record);
                }
                if (!records.isEmpty()) {
                    consumer.commitSync();
                }
            }
            log.info("PoolLoop остановлен вручную");
        } catch (WakeupException ignored) {
            log.warn("Возник WakeupException");
        } catch (Exception exp) {
            log.error("Ошибка чтения данных из топика {}", topic, exp);
        } finally {
            try {
                log.info("Закрываем Consumer");
                consumer.close();
            } catch (Exception exp) {
                log.warn("Ошибка при закрытии CONSUMER-a", exp);
            }
        }
    }

    @PreDestroy
    public void shutdown() {
        consumer.wakeup();
        isRunning = false;
    }

    private void handleRecord(ConsumerRecord<String, SensorsSnapshotAvro> record) {
        SensorsSnapshotAvro snapshot = record.value();
        log.info("Получили SNAPSHOT состояния умного дома: {}", snapshot);
        snapshotHandler.handle(snapshot);
    }
}
