package ru.yandex.practicum.kafka.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {
    @Autowired
    private final Environment environment;

    @Bean
    public Consumer<String, SensorsSnapshotAvro> getSnapsotConsumer() {
        Properties config = new Properties();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                environment.getProperty("analyzer.kafka.consumer.snapshots.properties.bootstrap-servers"));
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                environment.getProperty("analyzer.kafka.consumer.snapshots.properties.key-deserializer"));
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                environment.getProperty("analyzer.kafka.consumer.snapshots.properties.value-deserializer"));
        config.put(ConsumerConfig.CLIENT_ID_CONFIG,
                environment.getProperty("analyzer.kafka.consumer.snapshots.properties.client-id"));
        config.put(ConsumerConfig.GROUP_ID_CONFIG,
                environment.getProperty("analyzer.kafka.consumer.snapshots.properties.group-id"));
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
                environment.getProperty("analyzer.kafka.consumer.snapshots.properties.enable-auto-commit"));

        return new KafkaConsumer<>(config);
    }

    @Bean
    public Consumer<String, HubEventAvro> getHubEventConsumer() {
        Properties config = new Properties();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                environment.getProperty("analyzer.kafka.consumer.hub.properties.bootstrap-servers"));
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                environment.getProperty("analyzer.kafka.consumer.hub.properties.key-deserializer"));
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                environment.getProperty("analyzer.kafka.consumer.hub.properties.value-deserializer"));
        config.put(ConsumerConfig.CLIENT_ID_CONFIG,
                environment.getProperty("analyzer.kafka.consumer.hub.properties.client-id"));
        config.put(ConsumerConfig.GROUP_ID_CONFIG,
                environment.getProperty("analyzer.kafka.consumer.hub.properties.group-id"));
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
                environment.getProperty("analyzer.kafka.consumer.hub.properties.enable-auto-commit"));

        return new KafkaConsumer<>(config);
    }
}
