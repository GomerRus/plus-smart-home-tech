package ru.yandex.practicum.interaction.api.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookedProductsDto {
    @Positive
    @NotNull(message = "deliveryWeight не может быть NULL.")
    Double deliveryWeight;

    @Positive
    @NotNull(message = "deliveryVolume не может быть NULL.")
    Double deliveryVolume;

    @NotNull(message = "fragile не может быть NULL.")
    Boolean fragile;
}