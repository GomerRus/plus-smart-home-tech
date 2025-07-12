package ru.yandex.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.kafka.config.KafkaTopicsNames;
import ru.yandex.practicum.model.sensor.SensorEvent;
import ru.yandex.practicum.model.sensor.SwitchSensorEvent;
import ru.yandex.practicum.kafka.KafkaEventProducer;
import ru.yandex.practicum.service.mapper.sensor.SensorEventAvroMapper;
import ru.yandex.practicum.service.mapper.sensor.SensorEventProtoMapper;

@Component
public class SwitchSensorHandler extends BaseSensorHandler {

    public SwitchSensorHandler(KafkaEventProducer producer, KafkaTopicsNames topicsNames, SensorEventAvroMapper avroMapper, SensorEventProtoMapper protoMapper) {
        super(producer, topicsNames, avroMapper, protoMapper);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageSensorType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR;
    }


    @Override
    protected SensorEventAvro mapSensorEventToAvro(SensorEvent sensorEvent) {
        SwitchSensorAvro avro = avroMapper.mapSwitchSensorToAvro((SwitchSensorEvent) sensorEvent);
        return buildSensorEventAvro(sensorEvent, avro);
    }

    @Override
    protected SensorEvent mapSensorProtoToModel(SensorEventProto sensorProto) {
        SensorEvent sensor = protoMapper.mapSwitchSensorProtoToModel(sensorProto.getSwitchSensor());
        return mapBaseSensorProtoFieldsToSensor(sensor, sensorProto);
    }
}
