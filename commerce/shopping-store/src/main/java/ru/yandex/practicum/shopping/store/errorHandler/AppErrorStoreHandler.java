package ru.yandex.practicum.shopping.store.errorHandler;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.interaction.api.exception.AppError;
import ru.yandex.practicum.interaction.api.exception.store.ProductNotFoundException;

@Slf4j
@RestControllerAdvice
public class AppErrorStoreHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AppError handleProductNotFoundException(ProductNotFoundException exp) {
        log.warn(exp.getMessage(), exp);
        return new AppError("ОШИБКА: ПРОДУКТ НЕ НАЙДЕН " + exp.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError handleValidationException(MethodArgumentNotValidException exp) {
        log.error("ОШИБКА VALIDATION-STORE", exp);
        return new AppError("VALIDATION EXCEPTION" + exp.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError handleConstraintViolations(ConstraintViolationException exp) {
        log.error("ОШИБКА CONSTRAINT-STORE", exp);
        return new AppError("ОШИБКА ОГРАНИЧЕНИЙ CONSTRAINT " + exp.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AppError handleThrowable(Throwable exp) {
        log.error("ОШИБКА-STORE", exp);
        return new AppError("INTERNAL SERVER ERROR " + exp.getMessage());
    }
}
