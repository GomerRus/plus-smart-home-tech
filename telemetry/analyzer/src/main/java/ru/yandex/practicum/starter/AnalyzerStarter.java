package ru.yandex.practicum.starter;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.HubEventProcessor;
import ru.yandex.practicum.kafka.SnapshotProcessor;

@Component
@RequiredArgsConstructor
public class AnalyzerStarter implements CommandLineRunner {
    private final HubEventProcessor hubEventProcessor;
    private final SnapshotProcessor snapshotProcessor;

    @Override
    public void run(String... args) {
        Thread hubEventsThread = new Thread(hubEventProcessor);
        hubEventsThread.setName("HubEventThread");
        hubEventsThread.start();

        snapshotProcessor.start();
    }
}