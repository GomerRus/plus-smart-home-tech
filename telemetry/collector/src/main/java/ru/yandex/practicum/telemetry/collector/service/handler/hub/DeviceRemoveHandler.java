package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.config.KafkaTopicsNames;
import ru.yandex.practicum.telemetry.collector.model.hub.DeviceRemovedEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.enums.HubEventType;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaEventProducer;

@Component
public class DeviceRemoveHandler extends BaseHubHandler<DeviceRemovedEventAvro> {

    public DeviceRemoveHandler(KafkaEventProducer producer, KafkaTopicsNames topicsNames) {
        super(producer, topicsNames);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_REMOVED;
    }

    @Override
    public void handle(HubEvent hubEvent) {
        validEventType(hubEvent, DeviceRemovedEvent.class);
        super.handle(hubEvent);
    }

    @Override
    public DeviceRemovedEventAvro mapToAvro(HubEvent hubEvent) {
        DeviceRemovedEvent deviceRemovedEvent = (DeviceRemovedEvent) hubEvent;
        return DeviceRemovedEventAvro.newBuilder()
                .setId(deviceRemovedEvent.getId())
                .build();
    }

    @Override
    protected HubEventAvro mapToAvroHubEvent(HubEvent hubEvent) {
        DeviceRemovedEventAvro avro = mapToAvro(hubEvent);
        return buildHubEventAvro(hubEvent, avro);
    }
}
