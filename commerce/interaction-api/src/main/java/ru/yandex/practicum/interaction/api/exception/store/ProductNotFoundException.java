package ru.yandex.practicum.interaction.api.exception.store;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
