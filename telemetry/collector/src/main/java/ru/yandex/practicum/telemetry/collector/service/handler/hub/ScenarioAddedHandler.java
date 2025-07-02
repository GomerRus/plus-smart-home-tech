package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.telemetry.collector.kafka.config.KafkaTopicsNames;
import ru.yandex.practicum.telemetry.collector.model.hub.DeviceAction;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.ScenarioAddedEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.ScenarioCondition;
import ru.yandex.practicum.telemetry.collector.model.hub.enums.ActionType;
import ru.yandex.practicum.telemetry.collector.model.hub.enums.ConditionOperation;
import ru.yandex.practicum.telemetry.collector.model.hub.enums.ConditionType;
import ru.yandex.practicum.telemetry.collector.model.hub.enums.HubEventType;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaEventProducer;

import java.util.List;

@Component
public class ScenarioAddedHandler extends BaseHubHandler<ScenarioAddedEventAvro> {

    public ScenarioAddedHandler(KafkaEventProducer producer, KafkaTopicsNames topicsNames) {
        super(producer, topicsNames);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_ADDED;
    }

    @Override
    public void handle(HubEvent hubEvent) {
        validEventType(hubEvent, ScenarioAddedEvent.class);
        super.handle(hubEvent);
    }

    @Override
    public ScenarioAddedEventAvro mapToAvro(HubEvent hubEvent) {
        ScenarioAddedEvent scenarioAddedEvent = (ScenarioAddedEvent) hubEvent;
        List<ScenarioConditionAvro> scenarioConditionAvroList = scenarioAddedEvent.getConditions().stream()
                .map(this::mapToScenarioConditionAvro)
                .toList();
        List<DeviceActionAvro> actionAvroList = scenarioAddedEvent.getActions().stream()
                .map(this::mapToDeviceActionAvro)
                .toList();
        return ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEvent.getName())
                .setConditions(scenarioConditionAvroList)
                .setActions(actionAvroList)
                .build();
    }

    @Override
    protected HubEventAvro mapToAvroHubEvent(HubEvent hubEvent) {
        ScenarioAddedEventAvro avro = mapToAvro(hubEvent);
        return buildHubEventAvro(hubEvent, avro);
    }

    private ScenarioConditionAvro mapToScenarioConditionAvro(ScenarioCondition scenarioCondition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(scenarioCondition.getSensorId())
                .setType(mapToConditionTypeAvro(scenarioCondition.getType()))
                .setOperation(mapToConditionOperationAvro(scenarioCondition.getOperation()))
                .setValue(scenarioCondition.getValue())
                .build();
    }

    private ConditionTypeAvro mapToConditionTypeAvro(ConditionType conditionType) {
        return switch (conditionType) {
            case ConditionType.MOTION -> ConditionTypeAvro.MOTION;
            case ConditionType.LUMINOSITY -> ConditionTypeAvro.LUMINOSITY;
            case ConditionType.SWITCH -> ConditionTypeAvro.SWITCH;
            case ConditionType.TEMPERATURE -> ConditionTypeAvro.TEMPERATURE;
            case ConditionType.CO2LEVEL -> ConditionTypeAvro.CO2LEVEL;
            case ConditionType.HUMIDITY -> ConditionTypeAvro.HUMIDITY;
        };
    }

    private ConditionOperationAvro mapToConditionOperationAvro(ConditionOperation conditionOperation) {
        return switch (conditionOperation) {
            case ConditionOperation.EQUALS -> ConditionOperationAvro.EQUALS;
            case ConditionOperation.GREATER_THAN -> ConditionOperationAvro.GREATER_THAN;
            case ConditionOperation.LOWER_THAN -> ConditionOperationAvro.LOWER_THAN;
        };
    }

    private DeviceActionAvro mapToDeviceActionAvro(DeviceAction deviceAction) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(deviceAction.getSensorId())
                .setType(mapToActionTypeAvro(deviceAction.getType()))
                .setValue(deviceAction.getValue())
                .build();
    }

    private ActionTypeAvro mapToActionTypeAvro(ActionType actionType) {
        return switch (actionType) {
            case ActionType.ACTIVATE -> ActionTypeAvro.ACTIVATE;
            case ActionType.DEACTIVATE -> ActionTypeAvro.DEACTIVATE;
            case ActionType.INVERSE -> ActionTypeAvro.INVERSE;
            case ActionType.SET_VALUE -> ActionTypeAvro.SET_VALUE;
        };
    }
}
