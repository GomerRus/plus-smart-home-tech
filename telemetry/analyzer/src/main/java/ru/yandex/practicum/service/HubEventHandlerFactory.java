package ru.yandex.practicum.service;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.service.hub.HubEventHandler;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Component
public class HubEventHandlerFactory {
    private final Map<String, HubEventHandler> hubMap;

    public HubEventHandlerFactory(Set<HubEventHandler> hubSet) {
        this.hubMap = hubSet.stream()
                .collect(Collectors.toMap(
                        HubEventHandler::getPayloadType,
                        Function.identity()));
    }
}
