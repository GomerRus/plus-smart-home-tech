package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.yandex.practicum.telemetry.collector.config.KafkaConfig;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.enums.SensorEventType;
import ru.yandex.practicum.telemetry.collector.service.handler.KafkaEventProducer;

@Slf4j
public abstract class BaseSensorHandler implements SensorEventHandler {

    private KafkaEventProducer producer;
    private String topic;

    public BaseSensorHandler(KafkaEventProducer kafkaProducer, KafkaConfig kafkaConfig) {
        this.producer = kafkaProducer;
        topic = kafkaConfig.getTopics().get("sensors-events");
        if (topic == null) {
            throw new IllegalArgumentException("Тема sensors-events не настроена в конфигурации");
        }
    }

    @Override
    public void handle(SensorEvent sensorEvent) {
        log.debug("Начинаем обработку события: {}", sensorEvent);
        try {
            ProducerRecord<String, SpecificRecordBase> record =
                    new ProducerRecord<>(
                            topic,
                            null,
                            System.currentTimeMillis(),
                            sensorEvent.getHubId(),
                            mapToAvro(sensorEvent)
                    );
            producer.sendRecord(record);
            log.info("Событие успешно отправлено в Kafka");
        } catch (Exception e) {
            log.error("Ошибка при обработке события {}", sensorEvent, e);
            throw new RuntimeException("Ошибка при отправке события в Kafka", e);
        }
    }

    @Override
    public SensorEventType getMessageType() {
        throw new UnsupportedOperationException("Метод должен быть переопределен в наследнике");
    }

    abstract SpecificRecordBase mapToAvro(SensorEvent sensorEvent);
}
