package ru.yandex.practicum.delivery.service;

import ru.yandex.practicum.interaction.api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction.api.dto.order.OrderDto;

import java.util.UUID;

public interface DeliveryService {

    DeliveryDto createNewDelivery(DeliveryDto deliveryDto);

    void successfulDelivery(UUID deliveryId);

    void pickedProductsInDelivery(UUID deliveryId);

    void failedDelivery(UUID deliveryId);

    Double coastDelivery(OrderDto orderDto);
}