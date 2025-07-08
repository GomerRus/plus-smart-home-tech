package ru.yandex.practicum.exception.errorHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.exception.DuplicateException;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.exception.ValidationException;

@Slf4j
@RestControllerAdvice
public class AppErrorHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AppError handleNotFoundException(NotFoundException exp) {
        return new AppError("NOT FOUND EXCEPTION " + exp.getMessage());
    }

    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public AppError handleDuplicateException(DuplicateException exp) {
        return new AppError("DUPLICATE EXCEPTION " + exp.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError handleValidationException(ValidationException exp) {
        return new AppError("VALIDATION EXCEPTION" + exp.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError handleMethodArgumentException(MethodArgumentNotValidException exp) {
        return new AppError("EXCEPTION about ARGUMENT " + exp.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AppError handleThrowable(final Throwable e) {
        return new AppError("INTERNAL SERVER ERROR " + e.getMessage());
    }
}
