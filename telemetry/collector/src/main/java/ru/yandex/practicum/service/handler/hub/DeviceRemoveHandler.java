package ru.yandex.practicum.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.KafkaEventProducer;
import ru.yandex.practicum.kafka.config.KafkaConfig;
import ru.yandex.practicum.kafka.config.KafkaTopicsNames;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.model.hub.DeviceRemovedEvent;
import ru.yandex.practicum.model.hub.HubEvent;
import ru.yandex.practicum.service.mapper.hub.HubEventAvroMapper;
import ru.yandex.practicum.service.mapper.hub.HubEventProtoMapper;

@Component
public class DeviceRemoveHandler extends BaseHubHandler {
    public DeviceRemoveHandler(KafkaEventProducer producer, KafkaConfig kafkaProducerConfig, HubEventAvroMapper avroMapper, HubEventProtoMapper protoMapper) {
        super(producer, kafkaProducerConfig, avroMapper, protoMapper);
    }


    /*public DeviceRemoveHandler(KafkaEventProducer producer, KafkaTopicsNames topicsNames, HubEventAvroMapper avroMapper, HubEventProtoMapper protoMapper) {
        super(producer, topicsNames, avroMapper, protoMapper);
    }*/

    @Override
    public HubEventProto.PayloadCase getMessageHubType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }


    @Override
    protected HubEventAvro mapHubToAvro(HubEvent hubEvent) {
        DeviceRemovedEventAvro avro = avroMapper.mapDeviceRemoveToAvro((DeviceRemovedEvent) hubEvent);
        return buildHubEventAvro(hubEvent, avro);
    }

    @Override
    protected HubEvent mapHubProtoToModel(HubEventProto hubProto) {
        HubEvent hub = protoMapper.mapDeviceRemovedProtoToModel(hubProto.getDeviceRemoved());
        return mapBaseHubProtoFieldsToHub(hub, hubProto);
    }
}
