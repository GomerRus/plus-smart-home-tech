package ru.yandex.practicum.service.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.model.Action;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.Scenario;
import ru.yandex.practicum.model.ScenarioAction;
import ru.yandex.practicum.model.ScenarioActionId;
import ru.yandex.practicum.model.ScenarioCondition;
import ru.yandex.practicum.model.ScenarioConditionId;
import ru.yandex.practicum.model.Sensor;
import ru.yandex.practicum.repository.ActionRepository;
import ru.yandex.practicum.repository.ConditionRepository;
import ru.yandex.practicum.repository.ScenarioActionRepository;
import ru.yandex.practicum.repository.ScenarioConditionRepository;
import ru.yandex.practicum.repository.ScenarioRepository;
import ru.yandex.practicum.repository.SensorRepository;

@Component
@RequiredArgsConstructor
public class ScenarioAddedHandler implements HubEventHandler {

    private final ScenarioRepository scenarioRepository;
    private final ActionRepository actionRepository;
    private final ConditionRepository conditionRepository;
    private final SensorRepository sensorRepository;
    private final ScenarioActionRepository scenarioActionRepository;
    private final ScenarioConditionRepository scenarioConditionRepository;

    @Override
    public String getPayloadType() {
        return ScenarioAddedEventAvro.class.getSimpleName();
    }

    @Transactional
    @Override
    public void handle(HubEventAvro hub) {
        ScenarioAddedEventAvro scenarioAddedEvent = (ScenarioAddedEventAvro) hub.getPayload();

        Scenario scenario = scenarioRepository
                .findByHubIdAndName(hub.getHubId(), scenarioAddedEvent.getName())
                .orElseGet(() -> scenarioRepository.save(
                        Scenario.builder()
                                .hubId(hub.getHubId())
                                .name(scenarioAddedEvent.getName())
                                .build()));

        scenarioActionRepository.deleteByScenario(scenario);
        scenarioConditionRepository.deleteByScenario(scenario);

        scenarioAddedEvent.getConditions().forEach(cDto -> {
            Sensor sensor = sensorRepository.findById(cDto.getSensorId())
                    .orElseGet(() -> sensorRepository.save(
                            Sensor.builder()
                                    .id(cDto.getSensorId())
                                    .hubId(hub.getHubId())
                                    .build()));
            Condition condition = conditionRepository.save(
                    Condition.builder()
                            .type(cDto.getType())
                            .operation(cDto.getOperation())
                            .value(convertValue(cDto.getValue()))
                            .build());
            scenarioConditionRepository.save(
                    ScenarioCondition.builder()
                            .scenario(scenario)
                            .sensor(sensor)
                            .condition(condition)
                            .id(new ScenarioConditionId(
                                    scenario.getId(),
                                    sensor.getId(),
                                    condition.getId()))
                            .build());
        });
        scenarioAddedEvent.getActions().forEach(aDto -> {
            Sensor sensor = sensorRepository.findById(aDto.getSensorId())
                    .orElseGet(() -> sensorRepository.save(
                            Sensor.builder()
                                    .id(aDto.getSensorId())
                                    .hubId(hub.getHubId())
                                    .build()));
            Action action = actionRepository.save(
                    Action.builder()
                            .type(aDto.getType())
                            .value(aDto.getValue())
                            .build());
            scenarioActionRepository.save(
                    ScenarioAction.builder()
                            .scenario(scenario)
                            .sensor(sensor)
                            .action(action)
                            .id(new ScenarioActionId(
                                    scenario.getId(),
                                    sensor.getId(),
                                    action.getId()))
                            .build());
        });

    }

        private Integer convertValue(Object value) {
        return value instanceof Integer
                ? (Integer) value
                : ((Boolean) value ? 1 : 0);
    }
}























