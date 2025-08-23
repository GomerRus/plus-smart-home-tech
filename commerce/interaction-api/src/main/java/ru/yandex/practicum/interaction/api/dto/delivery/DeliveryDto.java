package ru.yandex.practicum.interaction.api.dto.delivery;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.interaction.api.dto.warehouse.AddressDto;
import ru.yandex.practicum.interaction.api.enums.DeliveryState;

import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryDto {
    @NotNull(message = "deliveryId не может быть NULL.")
    UUID deliveryId;

    @NotNull(message = "fromAddress не может быть NULL.")
    AddressDto fromAddress;

    @NotNull(message = "toAddress не может быть NULL.")
    AddressDto toAddress;

    @NotNull(message = "orderID не может быть NULL.")
    UUID orderID;

    @NotNull(message = "deliveryState не может быть NULL.")
    DeliveryState deliveryState;
}