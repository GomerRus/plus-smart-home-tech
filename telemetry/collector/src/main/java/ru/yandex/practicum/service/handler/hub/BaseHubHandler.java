package ru.yandex.practicum.service.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.KafkaEventProducer;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseHubHandler implements HubEventHandler {
    private final KafkaEventProducer producer;

    @Value("${collector.kafka.topic.telemetry-hubs}")
    private String topic;

    @Override
    public void handle(HubEventProto event) {
        producer.send(toAvro(event), event.getHubId(), mapTimestampToInstant(event), topic);
    }

    public Instant mapTimestampToInstant(HubEventProto event) {
        return Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos());
    }

    public abstract HubEventAvro toAvro(HubEventProto hubEvent);
}
