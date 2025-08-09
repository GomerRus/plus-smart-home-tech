package ru.yandex.practicum.interaction.api.exception.cart;

public class NotAuthorizedUserException extends RuntimeException {
    public NotAuthorizedUserException(String message) {
        super(message);
    }
}
