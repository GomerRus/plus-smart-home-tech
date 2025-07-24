package ru.yandex.practicum.service.hub;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.model.Sensor;
import ru.yandex.practicum.repository.SensorRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceAddedHandler implements HubEventHandler {

    private final SensorRepository sensorRepository;

    @Override
    public String getPayloadType() {
        return DeviceAddedEventAvro.class.getSimpleName();
    }

    @Transactional
    @Override
    public void handle(HubEventAvro hub) {
        log.info("Сохраняем новое устройство для HUB с ID = {}", hub.getHubId());
        sensorRepository.save(mapToSensor(hub));
    }

    private Sensor mapToSensor(HubEventAvro hub) {
        DeviceAddedEventAvro deviceAddedAvro = (DeviceAddedEventAvro) hub.getPayload();

        return Sensor.builder()
                .id(deviceAddedAvro.getId())
                .hubId(hub.getHubId())
                .build();
    }
}
