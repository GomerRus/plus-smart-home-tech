package ru.yandex.practicum.shopping.cart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.shopping.cart.model.ShoppingCart;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ShoppingCartMapper {
    ShoppingCart mapToCart(ShoppingCartDto shoppingCartDto);

    ShoppingCartDto mapToCartDto(ShoppingCart shoppingCart);

    // default Map<UUID, Integer> mapCartProducts(Map<UUID, Integer> products) {
    //    return products != null ? new HashMap<>(products) : Collections.emptyMap();
  //  }
}