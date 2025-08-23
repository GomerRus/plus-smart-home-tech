package ru.yandex.practicum.interaction.api.exception.warehouse;

public class OrderBookingNotFoundException extends RuntimeException {
    public OrderBookingNotFoundException(String message) {
        super(message);
    }
}
