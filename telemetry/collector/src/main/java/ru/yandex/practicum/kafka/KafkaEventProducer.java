package ru.yandex.practicum.kafka;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.serializer.GeneralAvroSerializer;

import java.time.Duration;
import java.util.Properties;

@Slf4j
@Component
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
            throw new IllegalArgumentException("Недопустимый ProducerParam=" + param);
        }
        try {
            ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                    param.getTopic(),
                    param.getPartition(),
                    param.getTimestamp(),
                    param.getKey(),
                    param.getValue());

            producer.send(record);
        } catch (Exception e) {
            log.error("Ошибка при отправке сообщения", e);
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