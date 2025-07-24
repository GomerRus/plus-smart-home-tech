package ru.yandex.practicum.service.hub;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubEventHandler {
    String getPayloadType();

    void handle(HubEventAvro hub);
}
