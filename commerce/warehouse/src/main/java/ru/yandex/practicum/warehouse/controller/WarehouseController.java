package ru.yandex.practicum.warehouse.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction.api.dto.warehouse.AddressDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.warehouse.service.WarehouseService;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
public class WarehouseController {
    private final WarehouseService warehouseService;

    @PutMapping
    public void newProduct(@Valid @RequestBody NewProductInWarehouseRequest newRequest) {
        log.info("Начинаем добавление нового продукта = {}", newRequest);
        warehouseService.newProduct(newRequest);
        log.info("Новый продукт добавлен УСПЕШНО = {}", newRequest);
    }

    @PostMapping("/check")
    public BookedProductsDto checkProducts(@Valid @RequestBody ShoppingCartDto shoppingCartDto) {
        log.info("Начинаем проверку кол-ва товаров на складе={}", shoppingCartDto);
        BookedProductsDto result = warehouseService.checkQuantityProducts(shoppingCartDto);
        log.info("Проверка кол-ва товара на складе прошла УСПЕШНО = {}, result = {}", shoppingCartDto, result);
        return result;
    }

    @PostMapping("/add")
    public void addProduct(@Valid @RequestBody AddProductToWarehouseRequest addRequest) {
        log.info("Принимаем товар на склад = {}", addRequest);
        warehouseService.addQuantityProduct(addRequest);
        log.info("Товар принят УСПЕШНО = {}", addRequest);
    }

    @GetMapping("/address")
    public AddressDto getAddress() {
        log.info("Запрашиваем адрес склада");
        AddressDto result = warehouseService.getAddress();
        log.info("Адрес склада  УСПЕШНО предоставлен = {}", result);
        return result;
    }
}











