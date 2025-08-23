package ru.yandex.practicum.interaction.api.dto.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewProductInWarehouseRequest {
    @NotNull(message = "productId не может быть NULL.")
    UUID productId;

    Boolean fragile;

    @NotNull(message = "dimension не может быть NULL.")
    DimensionDto dimension;

    @Min(1)
    @NotNull(message = "weight не может быть NULL.")
    Double weight;
}