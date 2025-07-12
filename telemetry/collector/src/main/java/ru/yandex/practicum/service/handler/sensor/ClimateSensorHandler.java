package ru.yandex.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.config.KafkaTopicsNames;
import ru.yandex.practicum.model.sensor.ClimateSensorEvent;
import ru.yandex.practicum.model.sensor.SensorEvent;
import ru.yandex.practicum.kafka.KafkaEventProducer;
import ru.yandex.practicum.service.mapper.sensor.SensorEventAvroMapper;
import ru.yandex.practicum.service.mapper.sensor.SensorEventProtoMapper;

@Component
public class ClimateSensorHandler extends BaseSensorHandler {

    public ClimateSensorHandler(KafkaEventProducer producer, KafkaTopicsNames topicsNames, SensorEventAvroMapper avroMapper, SensorEventProtoMapper protoMapper) {
        super(producer, topicsNames, avroMapper, protoMapper);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageSensorType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR;
    }

    @Override
    protected SensorEventAvro mapSensorEventToAvro(SensorEvent sensorEvent) {
        ClimateSensorAvro avro = avroMapper.mapClimateSensorToAvro((ClimateSensorEvent) sensorEvent);
        return buildSensorEventAvro(sensorEvent, avro);
    }

    @Override
    protected SensorEvent mapSensorProtoToModel(SensorEventProto sensorProto) {
        ClimateSensorEvent sensor = protoMapper.mapClimateSensorProtoToModel(sensorProto.getClimateSensor());
        return mapBaseSensorProtoFieldsToSensor(sensor, sensorProto);
    }
}
