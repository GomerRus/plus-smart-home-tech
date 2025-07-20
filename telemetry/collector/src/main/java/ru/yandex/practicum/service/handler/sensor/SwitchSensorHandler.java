package ru.yandex.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.kafka.KafkaEventProducer;

@Component
public class SwitchSensorHandler extends BaseSensorHandler {
    public SwitchSensorHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageSensorType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR_EVENT;
    }

    @Override
    public SensorEventAvro toAvro(SensorEventProto sensorEvent) {
        SwitchSensorProto switchSensor = sensorEvent.getSwitchSensorEvent();

        return SensorEventAvro.newBuilder()
                .setId(sensorEvent.getId())
                .setHubId(sensorEvent.getHubId())
                .setTimestamp(mapTimestampToInstant(sensorEvent))
                .setPayload(SwitchSensorAvro.newBuilder()
                        .setState(switchSensor.getState())
                        .build())
                .build();
    }
}