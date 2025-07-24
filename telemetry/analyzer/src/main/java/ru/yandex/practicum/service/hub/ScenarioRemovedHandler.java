package ru.yandex.practicum.service.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.model.Scenario;
import ru.yandex.practicum.repository.ScenarioActionRepository;
import ru.yandex.practicum.repository.ScenarioConditionRepository;
import ru.yandex.practicum.repository.ScenarioRepository;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScenarioRemovedHandler implements HubEventHandler {

    private final ScenarioActionRepository scenarioActionRepository;
    private final ScenarioConditionRepository scenarioConditionRepository;
    private final ScenarioRepository scenarioRepository;

    @Override
    public String getPayloadType() {
        return ScenarioRemovedEventAvro.class.getSimpleName();
    }

    @Transactional
    @Override
    public void handle(HubEventAvro hub) {
        ScenarioRemovedEventAvro scenarioRemovedEvent = (ScenarioRemovedEventAvro) hub.getPayload();

        Optional<Scenario> optScenario = scenarioRepository
                .findByHubIdAndName(hub.getHubId(), scenarioRemovedEvent.getName());
        if (optScenario.isPresent()) {
            Scenario scenario = optScenario.get();
            scenarioActionRepository.deleteByScenario(scenario);
            scenarioConditionRepository.deleteByScenario(scenario);
            scenarioRepository.delete(scenario);
            log.info("Удаляем сценарий из HUB с NAME = {} и с ID = {}",
                    scenarioRemovedEvent.getName(),
                    hub.getHubId());
        } else {
            log.info("Сценарий не найден");
        }
    }
}
