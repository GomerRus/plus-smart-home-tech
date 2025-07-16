package ru.yandex.practicum.service.mapper.hub;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ValueMapping;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.ConditionOperationProto;
import ru.yandex.practicum.grpc.telemetry.event.ConditionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioRemovedEventProto;
import ru.yandex.practicum.model.hub.DeviceAddedEvent;
import ru.yandex.practicum.model.hub.DeviceRemovedEvent;
import ru.yandex.practicum.model.hub.ScenarioAddedEvent;
import ru.yandex.practicum.model.hub.ScenarioCondition;
import ru.yandex.practicum.model.hub.ScenarioRemovedEvent;
import ru.yandex.practicum.model.hub.enums.ActionType;
import ru.yandex.practicum.model.hub.enums.ConditionOperation;
import ru.yandex.practicum.model.hub.enums.ConditionType;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HubEventProtoMapper {

    @Mapping(target = "deviceType", source = "type")
    @ValueMapping(target = "MOTION_SENSOR", source = "UNRECOGNIZED")
    DeviceAddedEvent mapDeviceAddedProtoToModel(DeviceAddedEventProto deviceAddedEventProto);

    DeviceRemovedEvent mapDeviceRemovedProtoToModel(DeviceRemovedEventProto deviceRemovedEventProto);

    @Mapping(target = "conditions", source = "conditionsList")
    @Mapping(target = "actions", source = "actionsList")
    ScenarioAddedEvent mapScenarioAddedProtoToModel(ScenarioAddedEventProto scenarioAddedEventProto);

    ScenarioRemovedEvent mapScenarioRemovedProtoToModel(ScenarioRemovedEventProto scenarioRemovedEventProto);

    @Mapping(target = "type", source = "type")
    @Mapping(target = "operation", source = "operation")
    @Mapping(target = "value", expression = "java(mapScenarioConditionProtoValueToModelValue(scenarioConditionProto))")
    ScenarioCondition mapScenarioConditionProtoToModel(ScenarioConditionProto scenarioConditionProto);

    @ValueMapping(target = "ACTIVATE", source = "UNRECOGNIZED")
    ActionType mapActionTypeProtoToModel(ActionTypeProto actionTypeProto);

    @ValueMapping(target = "EQUALS", source = "UNRECOGNIZED")
    ConditionOperation mapConditionOperationProtoToModel(ConditionOperationProto conditionOperationProto);

    @ValueMapping(target = "MOTION", source = "UNRECOGNIZED")
    ConditionType mapConditionTypeProtoToModel(ConditionTypeProto conditionTypeProto);

    @Named("mapScenarioConditionProtoValueToModelValue")
    default Object mapScenarioConditionProtoValueToModelValue(ScenarioConditionProto proto) {
        if (proto.hasBoolValue()) {
            return proto.getBoolValue();
        } else if (proto.hasIntValue()) {
            return proto.getIntValue();
        } else {
            return null;
        }
    }
}

