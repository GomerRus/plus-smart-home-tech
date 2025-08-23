package ru.yandex.practicum.delivery.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.delivery.model.Delivery;
import ru.yandex.practicum.interaction.api.dto.delivery.DeliveryDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DeliveryMapper {
    Delivery mapToDelivery(DeliveryDto deliveryDto);

    DeliveryDto mapToDeliveryDto(Delivery delivery);
}