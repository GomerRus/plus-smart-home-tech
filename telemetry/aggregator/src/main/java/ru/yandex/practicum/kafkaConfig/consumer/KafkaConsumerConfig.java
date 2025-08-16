package ru.yandex.practicum.kafkaConfig.consumer;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    @Autowired
    private final Environment environment;

    @Bean
    public KafkaConsumer<String, SpecificRecordBase> getConsumer() {
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                environment.getProperty("aggregator.kafka.consumer.properties.bootstrap-servers"));
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                environment.getProperty("aggregator.kafka.consumer.properties.key-deserializer"));
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                environment.getProperty("aggregator.kafka.consumer.properties.value-deserializer"));
        config.put(ConsumerConfig.GROUP_ID_CONFIG,
                environment.getProperty("aggregator.kafka.consumer.properties.group-id"));
        config.put(ConsumerConfig.CLIENT_ID_CONFIG,
                environment.getProperty("aggregator.kafka.consumer.properties.client-id"));
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
                environment.getProperty("aggregator.kafka.consumer.properties.enable-auto-commit"));
        return new KafkaConsumer<>(config);
    }
}
