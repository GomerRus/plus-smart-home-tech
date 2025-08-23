package ru.yandex.practicum.order.errorHandler;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.interaction.api.exception.AppError;
import ru.yandex.practicum.interaction.api.exception.cart.NotAuthorizedUserException;
import ru.yandex.practicum.interaction.api.exception.order.NoOrderFoundException;
import ru.yandex.practicum.interaction.api.exception.warehouse.NoSpecifiedProductInWarehouseException;

@Slf4j
@RestControllerAdvice
public class AppErrorOrderHandler {

    @ExceptionHandler(NotAuthorizedUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public AppError handleNotAuthorizedUser(NotAuthorizedUserException exp) {
        log.warn(exp.getMessage(), exp);
        return new AppError("ОШИБКА: ПОЛЬЗОВАТЕЛЬ НЕ АВТОРИЗОВАН " + exp.getMessage());
    }

    @ExceptionHandler(NoOrderFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError handleNoOrderFoundException(NoOrderFoundException exp) {
        log.warn(exp.getMessage(), exp);
        return new AppError("ОШИБКА: ЗАКАЗ НЕ НАЙДЕН " + exp.getMessage());
    }

    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError handleNoSpecifiedProductInWarehouseException(NoSpecifiedProductInWarehouseException exp) {
        log.warn(exp.getMessage(), exp);
        return new AppError("ОШИБКА: НЕТ УКАЗАННЫХ ТОВАРОВ НА СКЛАДЕ " + exp.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError handleValidationException(MethodArgumentNotValidException exp) {
        log.error("ОШИБКА VALIDATION-ORDER", exp);
        return new AppError("ORDER-VALIDATION EXCEPTION" + exp.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError handleConstraintViolations(ConstraintViolationException exp) {
        log.error("ОШИБКА CONSTRAINT-ORDER", exp);
        return new AppError("ORDER-ОШИБКА ОГРАНИЧЕНИЙ CONSTRAINT " + exp.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AppError handleThrowable(Throwable exp) {
        log.error("ОШИБКА-ORDER", exp);
        return new AppError("ORDER-INTERNAL SERVER ERROR " + exp.getMessage());
    }
}
