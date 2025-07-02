package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.kafka.config.KafkaTopicsNames;
import ru.yandex.practicum.telemetry.collector.model.sensor.MotionSensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.enums.SensorEventType;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaEventProducer;

@Component
public class MotionSensorHandler extends BaseSensorHandler<MotionSensorAvro> {

    public MotionSensorHandler(KafkaEventProducer producer, KafkaTopicsNames topicsNames) {
        super(producer, topicsNames);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEvent sensorEvent) {
        validEventType(sensorEvent, MotionSensorEvent.class);
        super.handle(sensorEvent);
    }

    @Override
    public MotionSensorAvro mapToAvro(SensorEvent sensorEvent) {
        MotionSensorEvent motionSensorEvent = (MotionSensorEvent) sensorEvent;
        return MotionSensorAvro.newBuilder()
                .setLinkQuality(motionSensorEvent.getLinkQuality())
                .setMotion(motionSensorEvent.getIsMotion())
                .setVoltage(motionSensorEvent.getVoltage())
                .build();
    }

    @Override
    protected SensorEventAvro mapToAvroSensorEvent(SensorEvent sensorEvent) {
        MotionSensorAvro avro = mapToAvro(sensorEvent);
        return buildSensorEventAvro(sensorEvent, avro);
    }
}
