package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.collector.config.KafkaConfig;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.enums.HubEventType;
import ru.yandex.practicum.telemetry.collector.service.handler.KafkaEventProducer;

@Slf4j
public abstract class BaseHubHandler<T extends SpecificRecordBase> implements HubEventHandler {

    private KafkaEventProducer producer;
    private String topic;

    public BaseHubHandler(KafkaEventProducer kafkaProducer, KafkaConfig kafkaConfig) {
        this.producer = kafkaProducer;
        topic = kafkaConfig.getTopics().get("hubs-events");
        if (topic == null) {
            throw new IllegalArgumentException("Тема hubs-events не настроена в конфигурации");
        }
    }

    @Override
    public void handle(HubEvent hubEvent) {
        log.debug("Начинаем обработку события: {}", hubEvent);
        try {
            ProducerRecord<String, SpecificRecordBase> record =
                    new ProducerRecord<>(
                            topic,
                            null,
                            System.currentTimeMillis(),
                            hubEvent.getHubId(),
                            mapToAvroHubEvent(hubEvent)
                    );
            producer.sendRecord(record);
            log.info("Событие успешно отправлено в Kafka");
        } catch (Exception e) {
            log.error("Ошибка при обработке события {}", hubEvent, e);
            throw new RuntimeException("Ошибка при отправке события в Kafka", e);
        }
    }

    @Override
    public HubEventType getMessageType() {
        throw new UnsupportedOperationException("Метод должен быть переопределен в наследнике");
    }

    protected HubEventAvro buildHubEventAvro(HubEvent hubEvent, T payloadAvro) {
        return HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(hubEvent.getTimestamp())
                .setPayload(payloadAvro)
                .build();
    }

    protected abstract SpecificRecordBase mapToAvro(HubEvent hubEvent);

    protected abstract HubEventAvro mapToAvroHubEvent(HubEvent hubEvent);
}