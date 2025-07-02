package ru.yandex.practicum.telemetry.collector.kafka;

import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.serializer.GeneralAvroSerializer;

import java.util.Properties;

@Getter
@Setter
@ToString
@Component
@Slf4j
public class KafkaEventProducer {
    private final Producer<String, SpecificRecordBase> producer;

    private KafkaEventProducer() {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class);
        this.producer = new KafkaProducer<>(config);
    }

    public void sendRecord(ProducerParam param) {
        if (!param.isValid()) {
            throw new IllegalArgumentException("invalid ProducerParam=" + param);
        }

        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                param.getTopic(),
                param.getPartition(),
                param.getTimestamp(),
                param.getKey(),
                param.getValue());

        producer.send(record);
    }

    @PreDestroy
    private void close() {
        if (producer != null) {
            producer.flush();
            producer.close();
        }
    }
}
    /* private final Producer<String, SpecificRecordBase> producer;

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
}*/