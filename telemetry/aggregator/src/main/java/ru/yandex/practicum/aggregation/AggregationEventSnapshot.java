package ru.yandex.practicum.aggregation;

import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Optional;

public interface AggregationEventSnapshot {

    Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event);
}
