package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.yandex.practicum.starter.AggregationStarter;

@SpringBootApplication
public class Aggregator {
    public static void main(String[] args) {
        SpringApplication.run(Aggregator.class, args);
    }
}
