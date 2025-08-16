package ru.yandex.practicum.shopping.cart.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction.api.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.shopping.cart.service.CartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-cart")
public class ShoppingCartController {
    public final CartService cartService;

    @GetMapping
    public ShoppingCartDto getShoppingCart(@RequestParam String username) {
        log.info("Получен GET /api/v1/shopping-cart запрос на получение корзины пользователя {}", username);
        return cartService.getShoppingCart(username);
    }

    @PutMapping
    public ShoppingCartDto addProductInCart(@RequestParam String username,
                                            @RequestBody @NotEmpty Map<UUID, @NotNull @Positive Integer> products) {
        log.info("Получен PUT /api/v1/shopping-cart запрос: с параметром username = {} и телом newProducts = {}",
                username, products);
        return cartService.addProductInCart(username, products);
    }

    @DeleteMapping
    public void deactivationShoppingCart(@RequestParam String username) {
        log.info("Получен DELETE /api/v1/shopping-cart запрос на деактивацию корзины товаров пользователя {}", username);
        cartService.deactivationShoppingCart(username);
    }


    @PostMapping("/remove")
    public ShoppingCartDto removeProductFromCart(@RequestParam String username,
                                                 @RequestBody @NotEmpty List<UUID> productsIds) {
        log.info("Получен POST /api/v1/shopping-cart запрос на удаление продуктов {} из корзины пользователя {}",
                productsIds, username);
        return cartService.removeProductFromCart(username, productsIds);
    }

    @PostMapping("change-quantity")
    public ShoppingCartDto changeQuantityInCart(@RequestParam String username,
                                                @Valid @RequestBody ChangeProductQuantityRequest quantityRequest) {
        log.info("Получен POST /api/v1/shopping-cart запрос на изменение количества товара в корзине пользователя {}", username);
        return cartService.changeQuantityInCart(username, quantityRequest);
    }
}









