package ru.yandex.practicum.service.mapper.sensor;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SensorEventProtoMapper {

    ClimateSensorAvro mapClimateSensorProtoToModel(ClimateSensorProto climateSensorProto);

    LightSensorAvro mapLightSensorProtoToModel(LightSensorProto lightSensorProto);

    MotionSensorAvro mapMotionSensorProtoToModel(MotionSensorProto motionSensorProto);

    SwitchSensorAvro mapSwitchSensorProtoToModel(SwitchSensorProto switchSensorProto);

    TemperatureSensorAvro mapTemperatureSensorProtoToModel(TemperatureSensorProto temperatureSensorProto);

}
