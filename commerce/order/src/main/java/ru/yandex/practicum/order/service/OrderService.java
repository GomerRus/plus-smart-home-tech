package ru.yandex.practicum.order.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.interaction.api.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.interaction.api.dto.order.ProductReturnRequest;

import java.util.UUID;

public interface OrderService {

    Page<OrderDto> getOrderByUsername(String username, Pageable pageable);

    OrderDto createNewOrder(String username, CreateNewOrderRequest createOrder);

    OrderDto returnOrderProducts(ProductReturnRequest productReturn);

    OrderDto paymentOrder(UUID orderId);

    OrderDto paymentOrderFailed(UUID orderId);

    OrderDto deliveryOrder(UUID orderId);

    OrderDto deliveryOrderFailed(UUID orderId);

    OrderDto completedOrder(UUID orderId);

    OrderDto calculateOrderTotalPrice(UUID orderId);

    OrderDto calculateOrderDeliveryPrice(UUID orderId);

    OrderDto assemblyOrder(UUID orderId);

    OrderDto assemblyOrderFailed(UUID orderId);
}
