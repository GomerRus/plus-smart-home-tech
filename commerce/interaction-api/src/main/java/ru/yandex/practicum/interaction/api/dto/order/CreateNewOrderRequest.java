package ru.yandex.practicum.interaction.api.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.AddressDto;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateNewOrderRequest {
    @NotNull(message = "shoppingCartDto не может быть NULL.")
    ShoppingCartDto shoppingCartDto;

    @NotNull(message = "deliveryAddress не может быть NULL.")
    AddressDto deliveryAddress;
}