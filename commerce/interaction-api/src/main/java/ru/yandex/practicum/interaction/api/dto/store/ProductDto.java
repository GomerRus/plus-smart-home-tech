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

    UUID productId;

    @NotBlank(message = "productName не может быть NULL или EMPTY.")
    String productName;

    String imageSrc;

    @NotNull(message = "quantityState не может быть NULL.")
    QuantityState quantityState;

    @NotNull(message = "productState не может быть NULL.")
    ProductState productState;

    ProductCategory productCategory;

    @Min(1)
    @NotNull(message = "price не может быть NULL.")
    Float price;

    @NotBlank(message = "description не может быть NULL или EMPTY.")
    String description;
}
