package ru.yandex.practicum.shopping.store.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.interaction.api.dto.store.ProductDto;
import ru.yandex.practicum.interaction.api.dto.store.ProductPageDto;
import ru.yandex.practicum.interaction.api.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.interaction.api.enums.ProductCategory;

import java.util.UUID;

public interface StoreService {
    ProductPageDto getAllProducts(ProductCategory category, Pageable pageable);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    Boolean removeProductById(UUID productId);

    Boolean setProductQuantityState(SetProductQuantityStateRequest stateRequest);

    ProductDto getProductById(UUID productId);
}