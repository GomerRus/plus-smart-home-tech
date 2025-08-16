package ru.yandex.practicum.interaction.api.feign.client.decoder.exception;

public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String message) {
        super(message);
    }
}
