package ru.yandex.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.kafka.config.KafkaTopicsNames;
import ru.yandex.practicum.model.sensor.SensorEvent;
import ru.yandex.practicum.model.sensor.SwitchSensorEvent;
import ru.yandex.practicum.model.sensor.enums.SensorEventType;
import ru.yandex.practicum.kafka.KafkaEventProducer;

@Component
public class SwitchSensorHandler extends BaseSensorHandler<SwitchSensorAvro> {

    public SwitchSensorHandler(KafkaEventProducer producer, KafkaTopicsNames topicsNames) {
        super(producer, topicsNames);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }

    @Override
    public SwitchSensorAvro mapToAvro(SensorEvent sensorEvent) {
        SwitchSensorEvent switchSensorEvent = (SwitchSensorEvent) sensorEvent;
        return SwitchSensorAvro.newBuilder()
                .setState(switchSensorEvent.getState())
                .build();
    }

    @Override
    protected SensorEventAvro mapToAvroSensorEvent(SensorEvent sensorEvent) {
        SwitchSensorAvro avro = mapToAvro(sensorEvent);
        return buildSensorEventAvro(sensorEvent, avro);
    }
}
