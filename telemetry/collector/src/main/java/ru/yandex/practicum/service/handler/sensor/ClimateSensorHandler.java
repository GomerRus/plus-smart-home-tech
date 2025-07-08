package ru.yandex.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.config.KafkaTopicsNames;
import ru.yandex.practicum.model.sensor.ClimateSensorEvent;
import ru.yandex.practicum.model.sensor.SensorEvent;
import ru.yandex.practicum.model.sensor.enums.SensorEventType;
import ru.yandex.practicum.kafka.KafkaEventProducer;

@Component
public class ClimateSensorHandler extends BaseSensorHandler<ClimateSensorAvro> {

    public ClimateSensorHandler(KafkaEventProducer producer, KafkaTopicsNames topicsNames) {
        super(producer, topicsNames);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }

    @Override
    public ClimateSensorAvro mapToAvro(SensorEvent sensorEvent) {
        ClimateSensorEvent climateSensorEvent = (ClimateSensorEvent) sensorEvent;
        return ClimateSensorAvro.newBuilder()
                .setTemperatureC(climateSensorEvent.getTemperatureC())
                .setHumidity(climateSensorEvent.getHumidity())
                .setCo2Level(climateSensorEvent.getCo2Level())
                .build();
    }

    @Override
    protected SensorEventAvro mapToAvroSensorEvent(SensorEvent sensorEvent) {
        ClimateSensorAvro avro = mapToAvro(sensorEvent);
        return buildSensorEventAvro(sensorEvent, avro);
    }
}
