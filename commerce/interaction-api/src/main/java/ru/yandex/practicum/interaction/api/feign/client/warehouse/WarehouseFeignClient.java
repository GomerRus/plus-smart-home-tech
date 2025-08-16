package ru.yandex.practicum.interaction.api.feign.client.warehouse;

import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction.api.dto.warehouse.AddressDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.NewProductInWarehouseRequest;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse", fallback = WarehouseFallback.class)
public interface WarehouseFeignClient {
    @PutMapping
    void newProduct(@Valid @RequestBody NewProductInWarehouseRequest newRequest) throws FeignException;

    @PostMapping("/check")
    BookedProductsDto checkQuantityProducts(@Valid @RequestBody ShoppingCartDto shoppingCartDto) throws FeignException;

    @PostMapping("/add")
    void addQuantityProduct(@Valid @RequestBody AddProductToWarehouseRequest addRequest) throws FeignException;

    @GetMapping("/address")
    AddressDto getAddress() throws FeignException;

}
