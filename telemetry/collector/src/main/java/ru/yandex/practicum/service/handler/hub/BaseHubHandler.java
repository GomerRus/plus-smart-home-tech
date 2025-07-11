package ru.yandex.practicum.service.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.KafkaEventProducer;
import ru.yandex.practicum.kafka.ProducerParam;
import ru.yandex.practicum.kafka.config.KafkaTopicsNames;
import ru.yandex.practicum.service.mapper.hub.HubEventProtoMapper;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseHubHandler implements HubEventHandler {

    protected final KafkaEventProducer producer;
    protected final KafkaTopicsNames topicsNames;
    protected final HubEventProtoMapper protoMapper;

    @Override
    public void handle(HubEventProto hubProto) {

        if (hubProto == null) {
            throw new IllegalArgumentException("HubEvent cannot be null");
        }
        HubEventAvro avro = mapHubProtoToAvro(hubProto);
        log.trace("map To AVRO confirm hubId={}", hubProto.getHubId());
        ProducerParam param = createProducerSendParam(hubProto, avro);
        log.trace("param created confirm hubId={}", hubProto.getHubId());
        producer.sendRecord(param);
        log.trace("record send confirm hubId={}", hubProto.getHubId());
    }

    protected HubEventAvro buildHubEventAvro(HubEventProto hubProto, SpecificRecordBase payloadAvro) {
        Instant timestamp = Instant.ofEpochSecond(hubProto.getTimestamp().getSeconds());
        return HubEventAvro.newBuilder()
                .setHubId(hubProto.getHubId())
                .setTimestamp(timestamp)
                .setPayload(payloadAvro)
                .build();
    }

    private ProducerParam createProducerSendParam(HubEventProto hubProto, HubEventAvro avro) {
        return ProducerParam.builder()
                .topic(topicsNames.getHubsTopic())
                .timestamp(hubProto.getTimestamp().getSeconds())
                .key(hubProto.getHubId())
                .value(avro)
                .build();
    }

    protected abstract HubEventAvro mapHubProtoToAvro(HubEventProto hubProto);
}
