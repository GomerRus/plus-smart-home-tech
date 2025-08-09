package ru.yandex.practicum.interaction.api.dto.store;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.interaction.api.enums.ProductCategory;
import ru.yandex.practicum.interaction.api.enums.ProductState;
import ru.yandex.practicum.interaction.api.enums.QuantityState;

import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDto {
    @NotNull
    UUID productId;

    @NotBlank
    String productName;

    @NotBlank
    String imageSrc;

    @NotNull
    QuantityState quantityState;

    @NotNull
    ProductState productState;

    @NotNull
    ProductCategory productCategory;

    @Min(1)
    Double price;
}
