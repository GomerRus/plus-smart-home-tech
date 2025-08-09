package ru.yandex.practicum.shopping.cart.errorHandler;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.interaction.api.exception.AppError;
import ru.yandex.practicum.interaction.api.exception.cart.CartNotFoundException;
import ru.yandex.practicum.interaction.api.exception.cart.NoProductsInShoppingCartException;
import ru.yandex.practicum.interaction.api.exception.cart.NotAuthorizedUserException;

@Slf4j
@RestControllerAdvice
public class AppErrorCartHandler {

    @ExceptionHandler(NoProductsInShoppingCartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError handleNoProductsInShoppingCart(NoProductsInShoppingCartException exp) {
        log.warn(exp.getMessage(), exp);
        return new AppError("ОШИБКА: НЕТ ПРОДУКТОВ В КОРЗИНЕ " + exp.getMessage());
    }

    @ExceptionHandler(NotAuthorizedUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public AppError handleNotAuthorizedUser(NotAuthorizedUserException exp) {
        log.warn(exp.getMessage(), exp);
        return new AppError("ОШИБКА: ПОЛЬЗОВАТЕЛЬ НЕ АВТОРИЗОВАН " + exp.getMessage());
    }

    @ExceptionHandler(CartNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError handleCartNotFound(CartNotFoundException exp) {
        log.warn(exp.getMessage(), exp);
        return new AppError("ОШИБКА: КОРЗИНА НЕ НАЙДЕНА " + exp.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError handleValidationException(MethodArgumentNotValidException exp) {
        log.error("ОШИБКА VALIDATION-CART", exp);
        return new AppError("VALIDATION EXCEPTION" + exp.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError handleConstraintViolations(ConstraintViolationException exp) {
        log.error("ОШИБКА CONSTRAINT-CART", exp);
        return new AppError("ОШИБКА ОГРАНИЧЕНИЙ CONSTRAINT " + exp.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AppError handleThrowable(Throwable exp) {
        log.error("ОШИБКА-CART", exp);
        return new AppError("INTERNAL SERVER ERROR " + exp.getMessage());
    }
}
