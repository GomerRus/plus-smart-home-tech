package ru.yandex.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.config.KafkaConfig;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.config.KafkaTopicsNames;
import ru.yandex.practicum.model.sensor.MotionSensorEvent;
import ru.yandex.practicum.model.sensor.SensorEvent;
import ru.yandex.practicum.kafka.KafkaEventProducer;
import ru.yandex.practicum.service.mapper.sensor.SensorEventAvroMapper;
import ru.yandex.practicum.service.mapper.sensor.SensorEventProtoMapper;

@Component
public class MotionSensorHandler extends BaseSensorHandler {
    public MotionSensorHandler(KafkaEventProducer producer, KafkaConfig kafkaProducerConfig, SensorEventAvroMapper avroMapper, SensorEventProtoMapper protoMapper) {
        super(producer, kafkaProducerConfig, avroMapper, protoMapper);
    }

  /*  public MotionSensorHandler(KafkaEventProducer producer, KafkaTopicsNames topicsNames, SensorEventAvroMapper avroMapper, SensorEventProtoMapper protoMapper) {
        super(producer, topicsNames, avroMapper, protoMapper);
    }*/

    public SensorEventProto.PayloadCase getMessageSensorType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR_EVENT;
    }

    @Override
    protected SensorEventAvro mapSensorEventToAvro(SensorEvent sensorEvent) {
        MotionSensorAvro avro = avroMapper.mapMotionSensorToAvro((MotionSensorEvent) sensorEvent);
        return buildSensorEventAvro(sensorEvent, avro);
    }

    @Override
    protected SensorEvent mapSensorProtoToModel(SensorEventProto sensorProto) {
        SensorEvent sensor = protoMapper.mapMotionSensorProtoToModel(sensorProto.getMotionSensorEvent());
        return mapBaseSensorProtoFieldsToSensor(sensor, sensorProto);
    }
}
