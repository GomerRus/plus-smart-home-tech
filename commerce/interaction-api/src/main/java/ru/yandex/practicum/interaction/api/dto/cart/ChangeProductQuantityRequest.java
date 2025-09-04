package ru.yandex.practicum.interaction.api.dto.cart;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangeProductQuantityRequest {
    @NotNull(message = "productID не может быть NULL.")
    UUID productId;

    @NotNull(message = "newQuantity не может быть NULL.")
    Integer newQuantity;
}
