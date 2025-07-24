package ru.yandex.practicum.aggregation;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class AggregationEventSnapshotImpl implements AggregationEventSnapshot {
    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        String hubId = event.getHubId();
        if (!snapshots.containsKey(hubId)) {
            SensorsSnapshotAvro snapshot = createNewSnapshot(event);
            snapshots.put(hubId, snapshot);
            return Optional.of(snapshot);
        } else {
            SensorsSnapshotAvro oldSnapshot = snapshots.get(hubId);
            Optional<SensorsSnapshotAvro> updatedSnapshotOpt = updateSnapshot(oldSnapshot, event);
            updatedSnapshotOpt.ifPresent(sensorsSnapshotAvro -> snapshots.put(hubId, sensorsSnapshotAvro));
            return updatedSnapshotOpt;
        }
    }

    private Optional<SensorsSnapshotAvro> updateSnapshot(SensorsSnapshotAvro oldSnapshot, SensorEventAvro event) {
        String sensorId = event.getId();
        if (oldSnapshot.getSensorsState().containsKey(sensorId)) {
            if (oldSnapshot.getSensorsState().get(sensorId).getTimestamp().isAfter(event.getTimestamp()) ||
                    oldSnapshot.getSensorsState().get(sensorId).getData().equals(event.getPayload())) {
                return Optional.empty();
            }
        }
        SensorStateAvro sensorState = createSensorState(event);
        oldSnapshot.getSensorsState().put(sensorId, sensorState);
        oldSnapshot.setTimestamp(event.getTimestamp());
        return Optional.of(oldSnapshot);
    }

    private SensorsSnapshotAvro buildSnapshot(SensorEventAvro event, Map<String, SensorStateAvro> states) {
        return SensorsSnapshotAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setSensorsState(states)
                .build();
    }

    private SensorStateAvro createSensorState(SensorEventAvro event) {
        return SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();
    }

    private SensorsSnapshotAvro createNewSnapshot(SensorEventAvro event) {
        Map<String, SensorStateAvro> sensorStates = new HashMap<>();
        sensorStates.put(event.getId(), createSensorState(event));
        return buildSnapshot(event, sensorStates);
    }
}
