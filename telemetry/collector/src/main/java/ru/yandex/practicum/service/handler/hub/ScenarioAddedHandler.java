package ru.yandex.practicum.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.config.KafkaTopicsNames;
import ru.yandex.practicum.kafka.KafkaEventProducer;
import ru.yandex.practicum.service.mapper.hub.HubEventProtoMapper;

@Component
public class ScenarioAddedHandler extends BaseHubHandler {

    public ScenarioAddedHandler(KafkaEventProducer producer,
                                KafkaTopicsNames topicsNames,
                                HubEventProtoMapper protoMapper) {
        super(producer, topicsNames, protoMapper);
    }

    @Override
    public HubEventProto.PayloadCase getMessageHubType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    protected HubEventAvro mapHubProtoToAvro(HubEventProto hubProto) {
        ScenarioAddedEventAvro avro = protoMapper.mapScenarioAddedProtoToModel(hubProto.getScenarioAdded());
        return buildHubEventAvro(hubProto, avro);
    }
}
