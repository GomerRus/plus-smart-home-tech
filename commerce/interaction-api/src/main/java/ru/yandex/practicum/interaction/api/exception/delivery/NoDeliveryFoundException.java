package ru.yandex.practicum.interaction.api.exception.delivery;

public class NoDeliveryFoundException extends RuntimeException {
    public NoDeliveryFoundException(String message) {
        super(message);
    }
}