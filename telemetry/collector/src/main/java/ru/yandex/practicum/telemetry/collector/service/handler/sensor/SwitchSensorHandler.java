package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.telemetry.collector.config.KafkaConfig;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SwitchSensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.enums.SensorEventType;
import ru.yandex.practicum.telemetry.collector.service.handler.KafkaEventProducer;

@Component
public class SwitchSensorHandler extends BaseSensorHandler {
    public SwitchSensorHandler(KafkaEventProducer kafkaProducer, KafkaConfig kafkaConfig) {
        super(kafkaProducer, kafkaConfig);
    }

    public SensorEventType getMessageType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }

    @Override
    public SwitchSensorAvro mapToAvro(SensorEvent sensorEvent) {
        SwitchSensorEvent switchSensorEvent = (SwitchSensorEvent) sensorEvent;
        return SwitchSensorAvro.newBuilder()
                .setState(switchSensorEvent.getIsState())
                .build();
    }
}
