package ru.yandex.practicum.service.handler.sensor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.ProducerParam;
import ru.yandex.practicum.kafka.config.KafkaTopicsNames;
import ru.yandex.practicum.model.sensor.SensorEvent;
import ru.yandex.practicum.model.sensor.enums.SensorEventType;
import ru.yandex.practicum.kafka.KafkaEventProducer;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseSensorHandler<T extends SpecificRecordBase> implements SensorEventHandler {
    protected final KafkaEventProducer producer;
    protected final KafkaTopicsNames topicsNames;

    @Override
    public void handle(SensorEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("HubEvent cannot be null");
        }
        log.trace("instance check confirm hubId={}", event.getHubId());
        SensorEventAvro avro = mapToAvroSensorEvent(event);
        log.trace("map To avro confirm hubId={}", event.getHubId());
        ProducerParam param = createProducerParam(event, avro);
        log.trace("param created confirm hubId={}", event.getHubId());
        producer.sendRecord(param);
        log.trace("record send confirm hubId={}", event.getHubId());
    }

    @Override
    public SensorEventType getMessageType() {
        throw new UnsupportedOperationException("Метод должен быть переопределен в наследнике");
    }

    protected SensorEventAvro buildSensorEventAvro(SensorEvent sensorEvent, T payloadAvro) {
        return SensorEventAvro.newBuilder()
                .setId(sensorEvent.getId())
                .setHubId(sensorEvent.getHubId())
                .setTimestamp(sensorEvent.getTimestamp())
                .setPayload(payloadAvro)
                .build();
    }

    private ProducerParam createProducerParam(SensorEvent event, SensorEventAvro avro) {
        return ProducerParam.builder()
                .topic(topicsNames.getSensorsTopic())
                .timestamp(event.getTimestamp().toEpochMilli())
                .key(event.getHubId())
                .value(avro)
                .build();
    }

    protected abstract SpecificRecordBase mapToAvro(SensorEvent sensorEvent);

    protected abstract SensorEventAvro mapToAvroSensorEvent(SensorEvent sensorEvent);
}
