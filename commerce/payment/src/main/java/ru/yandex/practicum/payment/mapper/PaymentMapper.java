package ru.yandex.practicum.payment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.interaction.api.dto.payment.PaymentDto;
import ru.yandex.practicum.payment.model.Payment;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {
    Payment mapToPayment(PaymentDto paymentDto);

    PaymentDto mapToPaymentDto(Payment payment);
}
