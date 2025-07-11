package ru.yandex.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.kafka.config.KafkaTopicsNames;
import ru.yandex.practicum.kafka.KafkaEventProducer;
import ru.yandex.practicum.service.mapper.sensor.SensorEventProtoMapper;

@Component
public class SwitchSensorHandler extends BaseSensorHandler {

    public SwitchSensorHandler(KafkaEventProducer producer,
                               KafkaTopicsNames topicsNames,
                               SensorEventProtoMapper protoMapper) {
        super(producer, topicsNames, protoMapper);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageSensorType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR;
    }

    @Override
    protected SensorEventAvro mapSensorProtoToAvro(SensorEventProto sensorProto) {
        SwitchSensorAvro avro = protoMapper.mapSwitchSensorProtoToModel(sensorProto.getSwitchSensor());
        return buildSensorEventAvro(sensorProto, avro);
    }
}
