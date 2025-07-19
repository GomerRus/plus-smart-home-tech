package ru.yandex.practicum.service.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.repository.SensorRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceRemovedHandler implements HubEventHandler {

    private final SensorRepository sensorRepository;

    @Override
    public String getPayloadType() {
        return DeviceRemovedEventAvro.class.getSimpleName();
    }

    @Transactional
    @Override
    public void handle(HubEventAvro hub) {
        DeviceRemovedEventAvro deviceRemovedAvro = (DeviceRemovedEventAvro) hub.getPayload();
        log.info("Удаляем устройство из HUBa с ID = {}  с hub_id = {}", deviceRemovedAvro.getId(), hub.getHubId());
        sensorRepository.deleteByIdAndHubId(deviceRemovedAvro.getId(), hub.getHubId());
    }
}

