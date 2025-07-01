package ru.yandex.practicum.telemetry.collector.model.sensor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.telemetry.collector.model.sensor.enums.SensorEventType;

@Getter
@Setter
@ToString(callSuper = true)
public class MotionSensorEvent extends SensorEvent {
    private Integer linkQuality;
    private Boolean isMotion;
    private Integer voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
