package ru.yandex.practicum.interaction.api.feign.client.warehouse;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction.api.dto.warehouse.AddressDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.NewProductInWarehouseRequest;

@Component
public class WarehouseFallback implements WarehouseFeignClient {

    @Override
    public void newProduct(NewProductInWarehouseRequest newRequest) {
        throw new WarehouseFallbackException("Fallback response: сервис WAREHOUSE временно недоступен");
    }

    @Override
    public BookedProductsDto checkQuantityProducts(ShoppingCartDto shoppingCartDto) {
        throw new WarehouseFallbackException("Fallback response: сервис WAREHOUSE временно недоступен");
    }

    @Override
    public void addQuantityProduct(AddProductToWarehouseRequest addRequest) {
        throw new WarehouseFallbackException("Fallback response: сервис WAREHOUSE временно недоступен");
    }

    @Override
    public AddressDto getAddress() {
        throw new WarehouseFallbackException("Fallback response: сервис WAREHOUSE временно недоступен");
    }
}
