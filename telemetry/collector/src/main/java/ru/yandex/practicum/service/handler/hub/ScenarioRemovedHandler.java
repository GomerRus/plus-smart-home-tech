package ru.yandex.practicum.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.config.KafkaConfig;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.kafka.config.KafkaTopicsNames;
import ru.yandex.practicum.kafka.KafkaEventProducer;
import ru.yandex.practicum.model.hub.HubEvent;
import ru.yandex.practicum.model.hub.ScenarioRemovedEvent;
import ru.yandex.practicum.service.mapper.hub.HubEventAvroMapper;
import ru.yandex.practicum.service.mapper.hub.HubEventProtoMapper;

@Component
public class ScenarioRemovedHandler extends BaseHubHandler {

    public ScenarioRemovedHandler(KafkaEventProducer producer, KafkaTopicsNames topicsNames, HubEventAvroMapper avroMapper, HubEventProtoMapper protoMapper) {
        super(producer, topicsNames, avroMapper, protoMapper);
    }

    @Override
    public HubEventProto.PayloadCase getMessageHubType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED;
    }


    @Override
    protected HubEventAvro mapHubToAvro(HubEvent hubEvent) {
        ScenarioRemovedEventAvro avro = avroMapper.mapScenarioRemovedToAvro((ScenarioRemovedEvent) hubEvent);
        return buildHubEventAvro(hubEvent, avro);
    }

    @Override
    protected HubEvent mapHubProtoToModel(HubEventProto hubProto) {
        ScenarioRemovedEvent hub = protoMapper.mapScenarioRemovedProtoToModel(hubProto.getScenarioRemoved());
        return mapBaseHubProtoFieldsToHub(hub, hubProto);
    }
}
