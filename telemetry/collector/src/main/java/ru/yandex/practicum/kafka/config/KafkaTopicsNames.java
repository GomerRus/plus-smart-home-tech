package ru.yandex.practicum.kafka.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
//@ConfigurationProperties(prefix = "kafka.topic.telemetry")
public class KafkaTopicsNames {
    private String sensorsTopic;
    private String hubsTopic;
}