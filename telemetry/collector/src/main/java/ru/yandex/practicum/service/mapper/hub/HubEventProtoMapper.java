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
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HubEventProtoMapper {

    @Mapping(target = "type", source = "type")
    @ValueMapping(target = "MOTION_SENSOR", source = "UNRECOGNIZED")
    DeviceAddedEventAvro mapDeviceAddedProtoToModel(DeviceAddedEventProto deviceAddedEventProto);

    DeviceRemovedEventAvro mapDeviceRemovedProtoToModel(DeviceRemovedEventProto deviceRemovedEventProto);

    @Mapping(target = "conditions", source = "conditionsList")
    @Mapping(target = "actions", source = "actionsList")
    ScenarioAddedEventAvro mapScenarioAddedProtoToModel(ScenarioAddedEventProto scenarioAddedEventProto);

    ScenarioRemovedEventAvro mapScenarioRemovedProtoToModel(ScenarioRemovedEventProto scenarioRemovedEventProto);

    @Mapping(target = "type", source = "type")
    @Mapping(target = "operation", source = "operation")
    @Mapping(target = "value", expression = "java(mapScenarioConditionProtoValueToModelValue(scenarioConditionProto))")
    ScenarioConditionAvro mapScenarioConditionProtoToModel(ScenarioConditionProto scenarioConditionProto);

    @ValueMapping(target = "ACTIVATE", source = "UNRECOGNIZED")
    ActionTypeAvro mapActionTypeProtoToModel(ActionTypeProto actionTypeProto);

    @ValueMapping(target = "EQUALS", source = "UNRECOGNIZED")
    ConditionOperationAvro mapConditionOperationProtoToModel(ConditionOperationProto conditionOperationProto);

    @ValueMapping(target = "MOTION", source = "UNRECOGNIZED")
    ConditionTypeAvro mapConditionTypeProtoToModel(ConditionTypeProto conditionTypeProto);

    @Named("mapScenarioConditionProtoValueToModelValue")
    default Object mapScenarioConditionProtoValueToModelValue(ScenarioConditionProto proto) {

        if (proto.hasIntValue()) {
            return proto.getIntValue();
        } else if (proto.hasBoolValue()) {
            return proto.getBoolValue();
        } else {
            return null;
        }
    }
}
