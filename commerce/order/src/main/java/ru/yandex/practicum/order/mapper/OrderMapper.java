package ru.yandex.practicum.order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.order.model.Order;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    Order mapToOrder(OrderDto orderDto);

    OrderDto mapToOrderDto(Order order);
}
