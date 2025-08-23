package ru.yandex.practicum.interaction.api.dto.payment;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDto {
    @NotNull(message = "paymentId не может быть NULL.")
    UUID paymentId;

    Double totalPayment;
    Double deliveryTotal;
    Double feeTotal;
}
