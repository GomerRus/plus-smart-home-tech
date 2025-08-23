package ru.yandex.practicum.interaction.api.exception.payment;

public class NoPaymentFoundException extends RuntimeException {
    public NoPaymentFoundException(String message) {
        super(message);
    }
}