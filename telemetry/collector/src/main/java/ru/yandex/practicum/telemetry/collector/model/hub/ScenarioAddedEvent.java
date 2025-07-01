package ru.yandex.practicum.telemetry.collector.model.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.telemetry.collector.model.hub.enums.HubEventType;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioAddedEvent extends HubEvent {
    @NotBlank
    private String name;

    @NotEmpty
    private List<ScenarioCondition> conditions;

    @NotEmpty
    private List<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
