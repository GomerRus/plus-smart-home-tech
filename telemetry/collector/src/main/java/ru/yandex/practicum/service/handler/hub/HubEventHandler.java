package ru.yandex.practicum.service.handler.hub;

import ru.yandex.practicum.model.hub.HubEvent;
import ru.yandex.practicum.model.hub.enums.HubEventType;

public interface HubEventHandler {

    void handle(HubEvent hubEvent);

    HubEventType getMessageType();
}
