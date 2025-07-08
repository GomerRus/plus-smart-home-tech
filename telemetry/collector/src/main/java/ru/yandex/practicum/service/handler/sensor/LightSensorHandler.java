package ru.yandex.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.config.KafkaTopicsNames;
import ru.yandex.practicum.model.sensor.LightSensorEvent;
import ru.yandex.practicum.model.sensor.SensorEvent;
import ru.yandex.practicum.model.sensor.enums.SensorEventType;
import ru.yandex.practicum.kafka.KafkaEventProducer;

@Component
public class LightSensorHandler extends BaseSensorHandler<LightSensorAvro> {

    public LightSensorHandler(KafkaEventProducer producer, KafkaTopicsNames topicsNames) {
        super(producer, topicsNames);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }

    @Override
    public LightSensorAvro mapToAvro(SensorEvent sensorEvent) {
        LightSensorEvent lightSensorEvent = (LightSensorEvent) sensorEvent;
        return LightSensorAvro.newBuilder()
                .setLinkQuality(lightSensorEvent.getLinkQuality())
                .setLuminosity(lightSensorEvent.getLuminosity())
                .build();
    }

    @Override
    protected SensorEventAvro mapToAvroSensorEvent(SensorEvent sensorEvent) {
        LightSensorAvro avro = mapToAvro(sensorEvent);
        return buildSensorEventAvro(sensorEvent, avro);
    }
}
