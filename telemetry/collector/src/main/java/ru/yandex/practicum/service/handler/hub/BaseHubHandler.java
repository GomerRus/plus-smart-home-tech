package ru.yandex.practicum.service.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.config.KafkaConfig;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.KafkaEventProducer;
import ru.yandex.practicum.kafka.ProducerParam;
import ru.yandex.practicum.kafka.config.KafkaTopicsNames;
import ru.yandex.practicum.model.hub.HubEvent;
import ru.yandex.practicum.service.mapper.hub.HubEventAvroMapper;
import ru.yandex.practicum.service.mapper.hub.HubEventProtoMapper;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseHubHandler implements HubEventHandler {

    protected final KafkaEventProducer producer;
   // protected final KafkaTopicsNames topicsNames;
    protected final KafkaConfig kafkaProducerConfig;
    protected final HubEventAvroMapper avroMapper;
    protected final HubEventProtoMapper protoMapper;

    protected abstract HubEventAvro mapHubToAvro(HubEvent hubEvent);

    protected abstract HubEvent mapHubProtoToModel(HubEventProto hubProto);

    @Override
    public void handle(HubEventProto hubProto) {

        if (hubProto == null) {
            throw new IllegalArgumentException("HubEvent cannot be null");
        }
        HubEvent event = mapHubProtoToModel(hubProto);
        log.trace("map To HUB confirm hubId={}", event.getHubId());
        HubEventAvro avro = mapHubToAvro(event);
        log.trace("map To AVRO confirm hubId={}", event.getHubId());
        ProducerParam param = createProducerSendParam(event, avro);
        log.trace("param created confirm hubId={}", event.getHubId());
        producer.sendRecord(param);
        log.trace("record send confirm hubId={}", event.getHubId());
    }

    protected HubEventAvro buildHubEventAvro(HubEvent hubEvent, SpecificRecordBase payloadAvro) {
        return HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(hubEvent.getTimestamp())
                .setPayload(payloadAvro)
                .build();
    }

    protected HubEvent mapBaseHubProtoFieldsToHub(HubEvent hub, HubEventProto hubProto) {
        hub.setHubId(hubProto.getHubId());

        long seconds = hubProto.getTimestamp().getSeconds();
        int nanos = hubProto.getTimestamp().getNanos();

        hub.setTimestamp(Instant.ofEpochSecond(seconds, nanos));
        return hub;
    }

    private ProducerParam createProducerSendParam(HubEvent event, HubEventAvro avro) {
        return ProducerParam.builder()
               // .topic(topicsNames.getHubsTopic())
                .topic(kafkaProducerConfig.getHubsTopic())
                .timestamp(event.getTimestamp().toEpochMilli())
                .key(event.getHubId())
                .value(avro)
                .build();
    }
}
