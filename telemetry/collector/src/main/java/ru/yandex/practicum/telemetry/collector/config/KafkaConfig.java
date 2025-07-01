package ru.yandex.practicum.telemetry.collector.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;

@Getter
@Setter
@ToString
@ConfigurationProperties("collector.kafka")
@Component
@Slf4j
public class KafkaConfig {
    private Map<String, String> topics;
    private Map<String, String> producerProperties;

    @PostConstruct
    public void init() {
        log.info("Инициализация KafkaConfig: {}", this);
    }

    @Bean
    public Properties getKafkaProperties() {
        Properties properties = new Properties();
        properties.putAll(producerProperties);
        return properties;
    }
}
