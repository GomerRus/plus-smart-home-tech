package ru.yandex.practicum.service.mapper.hub;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.model.hub.DeviceAddedEvent;
import ru.yandex.practicum.model.hub.DeviceRemovedEvent;
import ru.yandex.practicum.model.hub.ScenarioAddedEvent;
import ru.yandex.practicum.model.hub.ScenarioRemovedEvent;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HubEventAvroMapper {

    @Mapping(target = "type", source = "deviceType")
    DeviceAddedEventAvro mapDeviceAddedToAvro(DeviceAddedEvent deviceAddedEvent);

    DeviceRemovedEventAvro mapDeviceRemoveToAvro(DeviceRemovedEvent deviceRemovedEvent);

    ScenarioAddedEventAvro mapScenarioAddedToAvro(ScenarioAddedEvent scenarioAddedEvent);

    ScenarioRemovedEventAvro mapScenarioRemovedToAvro(ScenarioRemovedEvent scenarioRemovedEvent);
}