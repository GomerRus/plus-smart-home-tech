package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Sensor;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, String> {
    void deleteByIdAndHubId(String id, String hubId);

}
