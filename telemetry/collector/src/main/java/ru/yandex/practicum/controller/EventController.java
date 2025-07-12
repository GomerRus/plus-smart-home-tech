package ru.yandex.practicum.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import ru.yandex.practicum.service.handler.hub.HubEventHandler;
import ru.yandex.practicum.service.handler.sensor.SensorEventHandler;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@GrpcService
public class EventController extends CollectorControllerGrpc.CollectorControllerImplBase {

    private final Map<HubEventProto.PayloadCase, HubEventHandler> hubMapHandlers;
    private final Map<SensorEventProto.PayloadCase, SensorEventHandler> sensorMapHandlers;


    public EventController(Set<HubEventHandler> hubSetHandlers, Set<SensorEventHandler> sensorSetHandlers) {
        this.hubMapHandlers = hubSetHandlers.stream()
                .collect(Collectors.toMap(
                        HubEventHandler::getMessageHubType,
                        Function.identity()
                ));
        this.sensorMapHandlers = sensorSetHandlers.stream()
                .collect(Collectors.toMap(
                        SensorEventHandler::getMessageSensorType,
                        Function.identity()
                ));

    }

    @Override
    public void collectHubEvent(HubEventProto hubProto, StreamObserver<Empty> responseObserver) {
        try {
            if (hubMapHandlers.containsKey(hubProto.getPayloadCase())) {
                hubMapHandlers.get(hubProto.getPayloadCase()).handle(hubProto);
            } else {
                throw new IllegalArgumentException("Не могу найти обработчик для HUB " + hubProto.getPayloadCase());
            }
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(Status.fromThrowable(e)));
        }
    }

    @Override
    public void collectSensorEvent(SensorEventProto sensorProto, StreamObserver<Empty> responseObserver) {
        try {
            if (sensorMapHandlers.containsKey(sensorProto.getPayloadCase())) {
                sensorMapHandlers.get(sensorProto.getPayloadCase()).handle(sensorProto);
            } else {
                throw new IllegalArgumentException("Не могу найти обработчик для SENSOR " + sensorProto.getPayloadCase());
            }
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(Status.fromThrowable(e)));
        }
    }
}