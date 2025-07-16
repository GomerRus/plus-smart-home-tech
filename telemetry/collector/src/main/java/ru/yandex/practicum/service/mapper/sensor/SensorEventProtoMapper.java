package ru.yandex.practicum.service.mapper.sensor;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;
import ru.yandex.practicum.model.sensor.ClimateSensorEvent;
import ru.yandex.practicum.model.sensor.LightSensorEvent;
import ru.yandex.practicum.model.sensor.MotionSensorEvent;
import ru.yandex.practicum.model.sensor.SwitchSensorEvent;
import ru.yandex.practicum.model.sensor.TemperatureSensorEvent;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SensorEventProtoMapper {

    ClimateSensorEvent mapClimateSensorProtoToModel(ClimateSensorProto climateSensorProto);

    LightSensorEvent mapLightSensorProtoToModel(LightSensorProto lightSensorProto);

    MotionSensorEvent mapMotionSensorProtoToModel(MotionSensorProto motionSensorProto);

    SwitchSensorEvent mapSwitchSensorProtoToModel(SwitchSensorProto switchSensorProto);

    TemperatureSensorEvent mapTemperatureSensorProtoToModel(TemperatureSensorProto temperatureSensorProto);

}