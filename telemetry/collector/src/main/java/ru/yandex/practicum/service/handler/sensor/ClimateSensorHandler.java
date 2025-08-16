package ru.yandex.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.KafkaEventProducer;

@Component
public class ClimateSensorHandler extends BaseSensorHandler {

    public ClimateSensorHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageSensorType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR_EVENT;
    }

    @Override
    public SensorEventAvro toAvro(SensorEventProto sensorEvent) {
        ClimateSensorProto climateSensor = sensorEvent.getClimateSensorEvent();

        return SensorEventAvro.newBuilder()
                .setId(sensorEvent.getId())
                .setHubId(sensorEvent.getHubId())
                .setTimestamp(mapTimestampToInstant(sensorEvent))
                .setPayload(ClimateSensorAvro.newBuilder()
                        .setTemperatureC(climateSensor.getTemperatureC())
                        .setHumidity(climateSensor.getHumidity())
                        .setCo2Level(climateSensor.getCo2Level())
                        .build())
                .build();
    }

}
