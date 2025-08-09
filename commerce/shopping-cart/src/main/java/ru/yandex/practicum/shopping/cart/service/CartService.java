package ru.yandex.practicum.shopping.cart.service;

import ru.yandex.practicum.interaction.api.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CartService {
    ShoppingCartDto getShoppingCart(String userName);

    ShoppingCartDto addProductInCart(String userName, Map<UUID, Integer> products);

    ShoppingCartDto deactivationShoppingCart(String userName);

    ShoppingCartDto removeProductFromCart(String userName, List<UUID> productsIds);

    ShoppingCartDto changeQuantityInCart(String userName, ChangeProductQuantityRequest quantityRequest);

    }
