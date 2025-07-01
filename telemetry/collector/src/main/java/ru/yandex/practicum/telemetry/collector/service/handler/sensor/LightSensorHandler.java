package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.telemetry.collector.config.KafkaConfig;
import ru.yandex.practicum.telemetry.collector.model.sensor.LightSensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.enums.SensorEventType;
import ru.yandex.practicum.telemetry.collector.service.handler.KafkaEventProducer;

@Component
public class LightSensorHandler extends BaseSensorHandler {
    public LightSensorHandler(KafkaEventProducer kafkaProducer, KafkaConfig kafkaConfig) {
        super(kafkaProducer, kafkaConfig);
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
}
