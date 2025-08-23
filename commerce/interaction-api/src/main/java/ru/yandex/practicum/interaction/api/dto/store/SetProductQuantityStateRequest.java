package ru.yandex.practicum.interaction.api.dto.store;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.interaction.api.enums.QuantityState;

import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SetProductQuantityStateRequest {
    @NotNull(message = "productId не может быть NULL.")
    UUID productId;

    @NotNull(message = "quantityState не может быть NULL.")
    QuantityState quantityState;
}