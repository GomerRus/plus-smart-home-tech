package ru.yandex.practicum.service.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.KafkaEventProducer;
import ru.yandex.practicum.kafka.ProducerParam;
import ru.yandex.practicum.kafka.config.KafkaTopicsNames;
import ru.yandex.practicum.model.hub.HubEvent;
import ru.yandex.practicum.model.hub.enums.HubEventType;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseHubHandler<T extends SpecificRecordBase> implements HubEventHandler {

    protected final KafkaEventProducer producer;
    protected final KafkaTopicsNames topicsNames;

    @Override
    public void handle(HubEvent event) {

        if (event == null) {
            throw new IllegalArgumentException("HubEvent cannot be null");
        }
        log.trace("instance check confirm hubId={}", event.getHubId());
        HubEventAvro avro = mapToAvroHubEvent(event);
        log.trace("map To avro confirm hubId={}", event.getHubId());
        ProducerParam param = createProducerSendParam(event, avro);
        log.trace("param created confirm hubId={}", event.getHubId());
        producer.sendRecord(param);
        log.trace("record send confirm hubId={}", event.getHubId());
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

    private ProducerParam createProducerSendParam(HubEvent event, HubEventAvro avro) {
        return ProducerParam.builder()
                .topic(topicsNames.getHubsTopic())
                .timestamp(event.getTimestamp().toEpochMilli())
                .key(event.getHubId())
                .value(avro)
                .build();
    }

    protected abstract SpecificRecordBase mapToAvro(HubEvent hubEvent);

    protected abstract HubEventAvro mapToAvroHubEvent(HubEvent hubEvent);
}
