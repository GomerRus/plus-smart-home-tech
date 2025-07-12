package ru.yandex.practicum.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.config.KafkaTopicsNames;
import ru.yandex.practicum.kafka.KafkaEventProducer;
import ru.yandex.practicum.model.hub.DeviceAddedEvent;
import ru.yandex.practicum.model.hub.HubEvent;
import ru.yandex.practicum.service.mapper.hub.HubEventAvroMapper;
import ru.yandex.practicum.service.mapper.hub.HubEventProtoMapper;

@Component
public class DeviceAddedHandler extends BaseHubHandler {

    public DeviceAddedHandler(KafkaEventProducer producer, KafkaTopicsNames topicsNames, HubEventAvroMapper avroMapper, HubEventProtoMapper protoMapper) {
        super(producer, topicsNames, avroMapper, protoMapper);
    }

    @Override
    public HubEventProto.PayloadCase getMessageHubType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }

    @Override
    protected HubEventAvro mapHubToAvro(HubEvent hubEvent) {
        DeviceAddedEventAvro avro = avroMapper.mapDeviceAddedToAvro((DeviceAddedEvent) hubEvent);
        return buildHubEventAvro(hubEvent, avro);
    }

    @Override
    protected HubEvent mapHubProtoToModel(HubEventProto hubProto) {
        HubEvent hub = protoMapper.mapDeviceAddedProtoToModel(hubProto.getDeviceAdded());
        return mapBaseHubProtoFieldsToHub(hub, hubProto);
    }
}
