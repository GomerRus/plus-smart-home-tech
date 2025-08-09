package ru.yandex.practicum.interaction.api.feign.client.decoder.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
