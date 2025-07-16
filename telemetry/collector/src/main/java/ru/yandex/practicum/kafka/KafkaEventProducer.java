package ru.yandex.practicum.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.errorHandler.KafkaSendException;

@Slf4j
@Component
public class KafkaEventProducer implements DisposableBean {
    private final KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;
    private final Producer<String, SpecificRecordBase> kafkaProducer;

    public KafkaEventProducer(KafkaTemplate<String, SpecificRecordBase> kafkaTemplate, Producer<String, SpecificRecordBase> kafkaProducer) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaProducer = kafkaProducer;
    }

    public void sendRecord(ProducerParam param) {
        if (!param.isValid()) {
            throw new IllegalArgumentException("Недопустимый ProducerParam: " + param);
        }
        try {
            ProducerRecord<String, SpecificRecordBase> record = createProducerRecord(param);
            sendKafkaMessage(record);
        } catch (Exception e) {
            handleException(param, e);
        }
    }

    private ProducerRecord<String, SpecificRecordBase> createProducerRecord(ProducerParam param) {
        return new ProducerRecord<>(
                param.getTopic(),
                param.getPartition(),
                param.getTimestamp(),
                param.getKey(),
                param.getValue()
        );
    }

    private void sendKafkaMessage(ProducerRecord<String, SpecificRecordBase> record) throws Exception {
        try {
            kafkaTemplate.send(record).get();
        } catch (Exception e) {
            throw new KafkaSendException("Ошибка при отправке сообщения", e);
        }
    }

    private void handleException(ProducerParam param, Exception e) {
        log.error("Ошибка при отправке сообщения для param={}", param, e);
    }

    @Override
    public void destroy() throws Exception {
        try {
            kafkaTemplate.flush();
            kafkaProducer.flush();
            kafkaProducer.close();
            log.info("KafkaEventProducer корректно остановлен");
        } catch (Exception e) {
            log.error("Ошибка при закрытии KafkaEventProducer", e);
            throw e;
        }
    }
}