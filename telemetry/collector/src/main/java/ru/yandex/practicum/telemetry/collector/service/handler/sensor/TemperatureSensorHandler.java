package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.telemetry.collector.kafka.config.KafkaTopicsNames;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.TemperatureSensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.enums.SensorEventType;
import ru.yandex.practicum.telemetry.collector.kafka.KafkaEventProducer;

@Component
public class TemperatureSensorHandler extends BaseSensorHandler<TemperatureSensorAvro> {

    public TemperatureSensorHandler(KafkaEventProducer producer, KafkaTopicsNames topicsNames) {
        super(producer, topicsNames);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEvent sensorEvent) {
        validEventType(sensorEvent, TemperatureSensorEvent.class);
        super.handle(sensorEvent);
    }

    @Override
    public TemperatureSensorAvro mapToAvro(SensorEvent sensorEvent) {
        TemperatureSensorEvent temperatureSensorEvent = (TemperatureSensorEvent) sensorEvent;
        return TemperatureSensorAvro.newBuilder()
                .setTemperatureC(temperatureSensorEvent.getTemperatureC())
                .setTemperatureF(temperatureSensorEvent.getTemperatureF())
                .build();
    }

    @Override
    protected SensorEventAvro mapToAvroSensorEvent(SensorEvent sensorEvent) {
        TemperatureSensorAvro avro = mapToAvro(sensorEvent);
        return buildSensorEventAvro(sensorEvent, avro);
    }
}
