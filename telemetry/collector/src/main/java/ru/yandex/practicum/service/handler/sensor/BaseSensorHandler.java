package ru.yandex.practicum.service.handler.sensor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.ProducerParam;
import ru.yandex.practicum.kafka.config.KafkaTopicsNames;
import ru.yandex.practicum.kafka.KafkaEventProducer;
import ru.yandex.practicum.service.mapper.sensor.SensorEventProtoMapper;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseSensorHandler implements SensorEventHandler {
    protected final KafkaEventProducer producer;
    protected final KafkaTopicsNames topicsNames;
    protected final SensorEventProtoMapper protoMapper;

    @Override
    public void handle(SensorEventProto sensorProto) {
        if (sensorProto == null) {
            throw new IllegalArgumentException("HubEvent cannot be null");
        }
        SensorEventAvro avro = mapSensorProtoToAvro(sensorProto);
        log.trace("map To AVRO confirm hubId={}", sensorProto.getHubId());
        ProducerParam param = createProducerParam(sensorProto, avro);
        log.trace("param created confirm hubId={}", sensorProto.getHubId());
        producer.sendRecord(param);
        log.trace("record send confirm hubId={}", sensorProto.getHubId());
    }

    protected SensorEventAvro buildSensorEventAvro(SensorEventProto sensorProto, SpecificRecordBase payloadAvro) {
        Instant timestamp = Instant.ofEpochSecond(sensorProto.getTimestamp().getSeconds());
        return SensorEventAvro.newBuilder()
                .setId(sensorProto.getId())
                .setHubId(sensorProto.getHubId())
                .setTimestamp(timestamp)
                .setPayload(payloadAvro)
                .build();
    }

    private ProducerParam createProducerParam(SensorEventProto sensorProto, SensorEventAvro avro) {
        return ProducerParam.builder()
                .topic(topicsNames.getSensorsTopic())
                .timestamp(sensorProto.getTimestamp().getSeconds())
                .key(sensorProto.getHubId())
                .value(avro)
                .build();
    }

    protected abstract SensorEventAvro mapSensorProtoToAvro(SensorEventProto sensorProto);
}
