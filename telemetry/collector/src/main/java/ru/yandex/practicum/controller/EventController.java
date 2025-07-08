package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.hub.HubEvent;
import ru.yandex.practicum.model.hub.enums.HubEventType;
import ru.yandex.practicum.model.sensor.SensorEvent;
import ru.yandex.practicum.model.sensor.enums.SensorEventType;
import ru.yandex.practicum.service.handler.hub.HubEventHandler;
import ru.yandex.practicum.service.handler.sensor.SensorEventHandler;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/events", consumes = MediaType.APPLICATION_JSON_VALUE)
public class EventController {

    private final Map<HubEventType, HubEventHandler> hubEventHandlersMap;
    private final Map<SensorEventType, SensorEventHandler> sensorEventHandlersMap;

    public EventController(Set<HubEventHandler> hubEventHandlers, Set<SensorEventHandler> sensorEventHandlers) {
        this.hubEventHandlersMap = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageType, Function.identity()));
        this.sensorEventHandlersMap = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageType, Function.identity()));
    }

    @PostMapping("/hubs")
    public void collectHubEvent(@Valid @RequestBody HubEvent hub) {
        log.info("Получено HUBS событие типа: {}", hub.getType());
        if (hubEventHandlersMap.containsKey(hub.getType())) {
            hubEventHandlersMap.get(hub.getType()).handle(hub);
            log.info("HUBS cобытие успешно обработано");
        } else {
            log.error("HUB обработчик не найден для типа: {}", hub.getType());
            throw new IllegalArgumentException("HUB обработчик не найден");
        }
    }

    @PostMapping("/sensors")
    public void collectSensorEvent(@Valid @RequestBody SensorEvent sensor) {
        log.info("Получено SENSORS событие типа: {}", sensor.getType());
        if (sensorEventHandlersMap.containsKey(sensor.getType())) {
            sensorEventHandlersMap.get(sensor.getType()).handle(sensor);
            log.info("SENSORS cобытие успешно обработано");
        } else {
            log.error("SENSOR обработчик не найден для типа: {}", sensor.getType());
            throw new IllegalArgumentException("SENSOR обработчик не найден");
        }
    }
}
