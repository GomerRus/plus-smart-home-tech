package ru.yandex.practicum.interaction.api.dto.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.interaction.api.enums.OrderState;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto {
    @NotNull(message = "orderId не может быть NULL.")
    UUID orderId;

    UUID shoppingCartId;

    @NotNull(message = "Таблица products не может быть NULL.")
    @NotEmpty(message = "Таблица products не может быть EMPTY.")
    Map<@NotNull UUID, @NotNull @Positive Integer> products;

    UUID paymentId;
    UUID deliveryId;
    OrderState state;
    Double deliveryWeight;
    Double deliveryVolume;
    Boolean fragile;
    Double totalPrice;
    Double deliveryPrice;
    Double productPrice;
}