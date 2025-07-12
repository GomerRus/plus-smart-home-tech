package ru.yandex.practicum.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.config.KafkaTopicsNames;
import ru.yandex.practicum.kafka.KafkaEventProducer;
import ru.yandex.practicum.model.hub.HubEvent;
import ru.yandex.practicum.model.hub.ScenarioAddedEvent;
import ru.yandex.practicum.service.mapper.hub.HubEventAvroMapper;
import ru.yandex.practicum.service.mapper.hub.HubEventProtoMapper;

@Component
public class ScenarioAddedHandler extends BaseHubHandler {

    public ScenarioAddedHandler(KafkaEventProducer producer, KafkaTopicsNames topicsNames, HubEventAvroMapper avroMapper, HubEventProtoMapper protoMapper) {
        super(producer, topicsNames, avroMapper, protoMapper);
    }

    @Override
    public HubEventProto.PayloadCase getMessageHubType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }


    @Override
    protected HubEventAvro mapHubToAvro(HubEvent hubEvent) {
        ScenarioAddedEventAvro avro = avroMapper.mapScenarioAddedToAvro((ScenarioAddedEvent) hubEvent);
        return buildHubEventAvro(hubEvent, avro);
    }

    @Override
    protected HubEvent mapHubProtoToModel(HubEventProto hubProto) {
        HubEvent hub = protoMapper.mapScenarioAddedProtoToModel(hubProto.getScenarioAdded());
        return mapBaseHubProtoFieldsToHub(hub, hubProto);
    }
}
