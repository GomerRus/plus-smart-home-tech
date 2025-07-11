package ru.yandex.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.config.KafkaTopicsNames;
import ru.yandex.practicum.kafka.KafkaEventProducer;
import ru.yandex.practicum.service.mapper.sensor.SensorEventProtoMapper;

@Component
public class MotionSensorHandler extends BaseSensorHandler {

    public MotionSensorHandler(KafkaEventProducer producer,
                               KafkaTopicsNames topicsNames,
                               SensorEventProtoMapper protoMapper) {
        super(producer, topicsNames, protoMapper);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageSensorType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR;
    }

    @Override
    protected SensorEventAvro mapSensorProtoToAvro(SensorEventProto sensorProto) {
        MotionSensorAvro avro = protoMapper.mapMotionSensorProtoToModel(sensorProto.getMotionSensor());
        return buildSensorEventAvro(sensorProto, avro);
    }
}
