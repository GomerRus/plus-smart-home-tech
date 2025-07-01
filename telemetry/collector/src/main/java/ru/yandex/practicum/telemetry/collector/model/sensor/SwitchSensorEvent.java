package ru.yandex.practicum.telemetry.collector.model.sensor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.telemetry.collector.model.sensor.enums.SensorEventType;

@Getter
@Setter
@ToString(callSuper = true)
public class SwitchSensorEvent extends SensorEvent {
    private Boolean isState;

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
