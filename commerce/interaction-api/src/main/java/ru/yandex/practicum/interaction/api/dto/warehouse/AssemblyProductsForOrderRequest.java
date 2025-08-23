package ru.yandex.practicum.interaction.api.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssemblyProductsForOrderRequest {

    @NotNull(message = "Таблица products не может быть NULL.")
    Map<@NotNull UUID, @NotNull @Positive Integer> products;

    @NotNull(message = "orderId не может быть NULL.")
    UUID orderId;
}