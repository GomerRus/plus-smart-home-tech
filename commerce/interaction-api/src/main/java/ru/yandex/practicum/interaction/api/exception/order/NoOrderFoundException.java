package ru.yandex.practicum.interaction.api.exception.order;

public class NoOrderFoundException extends RuntimeException {
    public NoOrderFoundException(String message) {
        super(message);
    }
}