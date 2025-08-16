package ru.yandex.practicum.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.KafkaEventProducer;

@Component
public class DeviceAddedHandler extends BaseHubHandler {
    public DeviceAddedHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public HubEventProto.PayloadCase getMessageHubType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }

    @Override
    public HubEventAvro toAvro(HubEventProto hubEvent) {
        DeviceAddedEventProto deviceAddedEvent = hubEvent.getDeviceAdded();

        return HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(mapTimestampToInstant(hubEvent))
                .setPayload(new DeviceAddedEventAvro(deviceAddedEvent.getId(),
                        mapToDeviceTypeAvro(deviceAddedEvent.getType())))
                .build();
    }

    private DeviceTypeAvro mapToDeviceTypeAvro(DeviceTypeProto deviceTypeProto) {
        DeviceTypeAvro type = null;

        switch (deviceTypeProto) {
            case LIGHT_SENSOR -> type = DeviceTypeAvro.LIGHT_SENSOR;
            case MOTION_SENSOR -> type = DeviceTypeAvro.MOTION_SENSOR;
            case SWITCH_SENSOR -> type = DeviceTypeAvro.SWITCH_SENSOR;
            case CLIMATE_SENSOR -> type = DeviceTypeAvro.CLIMATE_SENSOR;
            case TEMPERATURE_SENSOR -> type = DeviceTypeAvro.TEMPERATURE_SENSOR;
        }

        return type;
    }
}