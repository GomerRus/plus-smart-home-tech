package ru.yandex.practicum.warehouse.service;

import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction.api.dto.warehouse.AddressDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.NewProductInWarehouseRequest;

public interface WarehouseService {
    void newProduct(NewProductInWarehouseRequest newRequest);

    BookedProductsDto checkQuantityProducts(ShoppingCartDto shoppingCartDto);

    void addQuantityProduct(AddProductToWarehouseRequest addRequest);

    AddressDto getAddress();
}