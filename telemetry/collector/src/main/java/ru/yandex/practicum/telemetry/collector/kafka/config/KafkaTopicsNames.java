package ru.yandex.practicum.telemetry.collector.kafka.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "kafka.topic.telemetry")
@Component
public class KafkaTopicsNames {
    private String hubsTopic;
    private String sensorsTopic;
}
