package ru.yandex.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.KafkaEventProducer;

@Component
public class LightSensorHandler extends BaseSensorHandler {

    public LightSensorHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageSensorType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR_EVENT;
    }

    @Override
    public SensorEventAvro toAvro(SensorEventProto sensorEvent) {
        LightSensorProto lightSensor = sensorEvent.getLightSensorEvent();

        return SensorEventAvro.newBuilder()
                .setId(sensorEvent.getId())
                .setHubId(sensorEvent.getHubId())
                .setTimestamp(mapTimestampToInstant(sensorEvent))
                .setPayload(LightSensorAvro.newBuilder()
                        .setLinkQuality(lightSensor.getLinkQuality())
                        .setLuminosity(lightSensor.getLuminosity())
                        .build())
                .build();
    }
}
