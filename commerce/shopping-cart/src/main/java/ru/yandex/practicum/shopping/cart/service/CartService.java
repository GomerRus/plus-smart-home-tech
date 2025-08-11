package ru.yandex.practicum.shopping.cart.service;

import ru.yandex.practicum.interaction.api.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CartService {
    ShoppingCartDto getShoppingCart(String username);

    ShoppingCartDto addProductInCart(String username, Map<UUID, Integer> products);

    void deactivationShoppingCart(String username);

    ShoppingCartDto removeProductFromCart(String username, List<UUID> productsIds);

    ShoppingCartDto changeQuantityInCart(String username, ChangeProductQuantityRequest quantityRequest);

}