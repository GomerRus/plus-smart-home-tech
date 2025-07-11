package ru.yandex.practicum.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.KafkaEventProducer;
import ru.yandex.practicum.kafka.config.KafkaTopicsNames;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.service.mapper.hub.HubEventProtoMapper;

@Component
public class DeviceRemoveHandler extends BaseHubHandler {

    public DeviceRemoveHandler(KafkaEventProducer producer,
                               KafkaTopicsNames topicsNames,
                               HubEventProtoMapper protoMapper) {
        super(producer, topicsNames, protoMapper);
    }

    @Override
    public HubEventProto.PayloadCase getMessageHubType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }

    @Override
    protected HubEventAvro mapHubProtoToAvro(HubEventProto hubProto) {
        DeviceRemovedEventAvro avro = protoMapper.mapDeviceRemovedProtoToModel(hubProto.getDeviceRemoved());
        return buildHubEventAvro(hubProto, avro);
    }
}
