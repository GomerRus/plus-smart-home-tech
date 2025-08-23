package ru.yandex.practicum.payment.errorHandler;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.interaction.api.exception.AppError;
import ru.yandex.practicum.interaction.api.exception.order.NoOrderFoundException;
import ru.yandex.practicum.interaction.api.exception.payment.NoPaymentFoundException;
import ru.yandex.practicum.interaction.api.exception.payment.NotEnoughInfoInOrderToCalculateException;

@Slf4j
@RestControllerAdvice
public class AppErrorPaymentHandler {

    @ExceptionHandler(NoPaymentFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AppError handleNoPaymentFoundException(NoPaymentFoundException exp) {
        log.warn(exp.getMessage(), exp);
        return new AppError("ОШИБКА: Сведений об оплате не найдено " + exp.getMessage());
    }

    @ExceptionHandler(NotEnoughInfoInOrderToCalculateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError handleNotEnoughInfoInOrderToCalculateException(NotEnoughInfoInOrderToCalculateException exp) {
        log.warn(exp.getMessage(), exp);
        return new AppError("ОШИБКА: НЕДОСТАТОЧНО ИНФОРМАЦИИ В ЗАКАЗЕ ДЛЯ РАСЧЕТА " + exp.getMessage());
    }

    @ExceptionHandler(NoOrderFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AppError handleNoOrderFoundException(NoOrderFoundException exp) {
        log.warn(exp.getMessage(), exp);
        return new AppError("ОШИБКА: ЗАКАЗ НЕ НАЙДЕН " + exp.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError handleValidationException(MethodArgumentNotValidException exp) {
        log.error("ОШИБКА VALIDATION-PAYMENT", exp);
        return new AppError("PAYMENT-VALIDATION EXCEPTION" + exp.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError handleConstraintViolations(ConstraintViolationException exp) {
        log.error("ОШИБКА CONSTRAINT-PAYMENT", exp);
        return new AppError("PAYMENT-ОШИБКА ОГРАНИЧЕНИЙ CONSTRAINT " + exp.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AppError handleThrowable(Throwable exp) {
        log.error("ОШИБКА-PAYMENT", exp);
        return new AppError("PAYMENT-INTERNAL SERVER ERROR " + exp.getMessage());
    }
}

