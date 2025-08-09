package ru.yandex.practicum.interaction.api.feign.client.warehouse;

public class WarehouseFallbackException extends RuntimeException {
    public WarehouseFallbackException(String message) {
        super(message);
    }
}
