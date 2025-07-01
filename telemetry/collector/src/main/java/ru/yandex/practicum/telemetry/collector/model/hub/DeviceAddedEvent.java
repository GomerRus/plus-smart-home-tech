package ru.yandex.practicum.telemetry.collector.model.hub;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.telemetry.collector.model.hub.enums.DeviceType;
import ru.yandex.practicum.telemetry.collector.model.hub.enums.HubEventType;

@Getter
@Setter
@ToString(callSuper = true)
public class DeviceAddedEvent extends HubEvent {
    @NotBlank
    private String id;
    private DeviceType deviceType;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }
}
