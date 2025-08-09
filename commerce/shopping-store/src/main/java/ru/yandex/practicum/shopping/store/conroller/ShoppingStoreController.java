package ru.yandex.practicum.shopping.store.conroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction.api.dto.store.ProductDto;
import ru.yandex.practicum.interaction.api.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.interaction.api.enums.ProductCategory;
import ru.yandex.practicum.shopping.store.service.StoreService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-store")
public class ShoppingStoreController {
    private final StoreService storeService;

    @GetMapping
    public List<ProductDto> getAllProducts(@RequestParam ProductCategory category, Pageable pageable) {
        log.info("Получен GET /api/v1/shopping-store запрос c категорией = {} и pageable = {}", category, pageable);
        return storeService.getAllProducts(category, pageable);
    }

    @PutMapping
    public ProductDto createProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("Получен PUT /api/v1/shopping-store запрос на добавление товара c productName = {}",
                productDto.getProductName());
        return storeService.createProduct(productDto);
    }

    @PostMapping
    public ProductDto updateProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("Получен PUT /api/v1/shopping-store запрос на обновление товара c productName = {}",
                productDto.getProductName());
        return storeService.updateProduct(productDto);
    }

    @PostMapping("/removeProductFromStore")
    public Boolean removeProductById(@RequestBody UUID productId) {
        log.info("Получен POST /api/v1/shopping-store запрос на деактивацию товара с ID = {}", productId);
        return storeService.removeProductById(productId);
    }

    @PostMapping("/quantityState")
    public Boolean setProductQuantityState(@RequestParam SetProductQuantityStateRequest stateRequest) {
        return storeService.setProductQuantityState(stateRequest);
    }

    @GetMapping("/{productId}")
    public ProductDto getProductById(@PathVariable UUID productId) {
        log.info("Получен GET /api/v1/shopping-store запрос на получение информации о товаре с ID = {}", productId);
        return storeService.getProductById(productId);
    }
}













