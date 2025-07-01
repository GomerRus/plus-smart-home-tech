package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.telemetry.collector.config.KafkaConfig;
import ru.yandex.practicum.telemetry.collector.model.sensor.ClimateSensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.enums.SensorEventType;
import ru.yandex.practicum.telemetry.collector.service.handler.KafkaEventProducer;

@Component
public class ClimateSensorHandler extends BaseSensorHandler {
    public ClimateSensorHandler(KafkaEventProducer kafkaProducer, KafkaConfig kafkaConfig) {
        super(kafkaProducer, kafkaConfig);
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
}
