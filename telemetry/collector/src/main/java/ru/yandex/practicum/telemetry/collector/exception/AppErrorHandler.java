package ru.yandex.practicum.telemetry.collector.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AppError handleThrowable(final Throwable e) {
        return new AppError("INTERNAL SERVER ERROR " + e.getMessage());
    }
}
