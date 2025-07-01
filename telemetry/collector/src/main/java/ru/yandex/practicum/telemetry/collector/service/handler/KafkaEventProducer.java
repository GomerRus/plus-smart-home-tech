package ru.yandex.practicum.telemetry.collector.service.handler;

import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.collector.config.KafkaConfig;

import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.Future;

@Getter
@Setter
@ToString
@Component
@Slf4j
public class KafkaEventProducer {

    private final Producer<String, SpecificRecordBase> producer;

    public KafkaEventProducer(KafkaConfig kafkaConfig) {
        Properties properties = kafkaConfig.getKafkaProperties();
        log.info("Инициализация KafkaProducer с настройками: {}", properties);
        this.producer = new KafkaProducer<>(properties);
    }


    public Future<RecordMetadata> sendRecord(ProducerRecord<String, SpecificRecordBase> record) {
        try {
            Future<RecordMetadata> future = producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    log.error("Ошибка при отправке сообщения", exception);
                } else {
                    log.info("Сообщение отправлено в партицию {} с offset {}",
                            metadata.partition(), metadata.offset());
                }
            });

            producer.flush();
            return future;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при отправке сообщения", e);
        }
    }

    public void close(Duration timeout) {
        try {
            producer.close(timeout);
            log.info("Producer успешно закрыт");
        } catch (Exception e) {
            log.error("Ошибка при закрытии producer", e);
        }
    }

    @PreDestroy
    public void destroy() {
        close(Duration.ofSeconds(30));
    }
}