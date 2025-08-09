package ru.yandex.practicum.shopping.cart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.shopping.cart.model.ShoppingCart;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ShoppingCartMapper {
    ShoppingCart mapToCart(ShoppingCartDto shoppingCartDto);

    ShoppingCartDto mapToCartDto(ShoppingCart shoppingCart);
}