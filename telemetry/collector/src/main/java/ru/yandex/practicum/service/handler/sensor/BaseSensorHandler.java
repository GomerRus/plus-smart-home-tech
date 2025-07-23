package ru.yandex.practicum.service.handler.sensor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.KafkaEventProducer;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseSensorHandler implements SensorEventHandler {
    private final KafkaEventProducer producer;

    @Value("${collector.kafka.topic.telemetry-sensors}")
    private String topic;

    @Override
    public void handle(SensorEventProto event) {
        SensorEventAvro sensorEventAvro = toAvro(event);
        log.info("Send {}", sensorEventAvro);
        producer.send(sensorEventAvro, event.getHubId(), mapTimestampToInstant(event), topic);
    }

    public abstract SensorEventAvro toAvro(SensorEventProto sensorEvent);

    public Instant mapTimestampToInstant(SensorEventProto event) {
        return Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos());
    }
}
