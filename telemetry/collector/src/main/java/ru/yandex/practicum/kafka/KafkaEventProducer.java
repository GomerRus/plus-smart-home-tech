package ru.yandex.practicum.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventProducer implements AutoCloseable {
    private final Producer<String, SpecificRecordBase> producer;

    public void send(SpecificRecordBase message, String hubId, Instant timestamp, String topic) {
        ProducerRecord<String, SpecificRecordBase> record =
                new ProducerRecord<>(topic, null, timestamp.toEpochMilli(), hubId, message);

        producer.send(record, logCallback(topic, hubId, message));
    }

    private Callback logCallback(String topic, String hubId, SpecificRecordBase message) {
        return (RecordMetadata metadata, Exception ex) -> {
            if (ex != null) {
                log.error("Не удалось отправить сообщение в Kafka. topic={}, hubId={}", topic, hubId, ex);
            } else {
                log.info("Отправлено сообщение в Kafka: topic={}, partition={}, offset={}, hubId={}",
                        metadata.topic(), metadata.partition(), metadata.offset(), hubId);
            }
        };
    }

    @Override
    public void close() {
        producer.flush();
        producer.close(Duration.ofMillis(1000));
    }
}