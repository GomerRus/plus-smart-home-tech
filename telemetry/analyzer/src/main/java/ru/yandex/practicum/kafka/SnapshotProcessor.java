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
public class SnapshotProcessor implements Runnable {

    private final Consumer<String, SensorsSnapshotAvro> consumer;
    private final SnapshotHandler snapshotHandler;
    private volatile boolean isRunning = true;

    @Value("${analyzer.topic.snapshots-topic}")
    private String topic;

    @Override
    public void run() {
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

        try {
            consumer.subscribe(List.of(topic));
            while (isRunning) {

                ConsumerRecords<String, SensorsSnapshotAvro> records = consumer.poll(Duration.ofMillis(1000));
                consumer.commitSync();

                for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                    handleRecord(record);
                }
            }
            log.info("PoolLoop остановлен вручную");
        } catch (WakeupException ignored) {
            log.warn("Возник WakeupException");
        } catch (Exception exp) {
            log.error("Ошибка во время обработки событий от датчиков", exp);
        } finally {
            log.info("Закрываем Consumer");
            consumer.close();
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
