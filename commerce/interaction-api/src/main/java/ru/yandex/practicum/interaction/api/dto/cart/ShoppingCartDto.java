package ru.yandex.practicum.interaction.api.dto.cart;

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
public class ShoppingCartDto {
    @NotNull(message = "cartId не может быть NULL.")
    UUID cartId;

    @NotNull(message = "Таблица products не может быть NULL.")
    Map<UUID, @NotNull @Positive Integer> products;
}