package ru.yandex.practicum.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction.api.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.interaction.api.dto.order.ProductReturnRequest;
import ru.yandex.practicum.order.service.OrderService;

import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public Page<OrderDto> getOrderByUsername(@Valid @RequestParam String username,
                                             @RequestParam @PageableDefault(size = 10,
                                                     page = 0,
                                                     direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Получить заказ пользователя {}", username);
        return orderService.getOrderByUsername(username, pageable);
    }

    @PutMapping
    public OrderDto createNewOrder(@RequestParam String username,
                                   @Valid @RequestBody CreateNewOrderRequest createOrder) {
        log.info("Пользователь {}, создает новый заказ {}.", username, createOrder);
        return orderService.createNewOrder(username, createOrder);
    }

    @PostMapping("/return")
    public OrderDto returnOrderProducts(@Valid @RequestBody ProductReturnRequest productReturn) {
        log.info("Начинаем возврат товара по заказу с ID = {}.", productReturn.getOrderId());
        return orderService.returnOrderProducts(productReturn);
    }

    @PostMapping("/payment")
    public OrderDto paymentOrder(@RequestBody UUID orderId) {
        log.info("Начинаем оплату заказа с ID = {}", orderId);
        return orderService.paymentOrder(orderId);
    }

    @PostMapping("/payment/failed")
    public OrderDto paymentOrderFailed(@RequestBody UUID orderId) {
        log.info("Возникла ОШИБКА при оплате заказа с ID = {}", orderId);
        return orderService.paymentOrderFailed(orderId);
    }

    @PostMapping("/delivery")
    public OrderDto deliveryOrder(@RequestBody UUID orderId) {
        log.info("Передаем заказ с ID = {} в доставку", orderId);
        return orderService.deliveryOrder(orderId);
    }

    @PostMapping("/delivery/failed")
    public OrderDto deliveryOrderFailed(@RequestBody UUID orderId) {
        log.info("Заказа с ID = {} получил статус доставки = FAILED.", orderId);
        return orderService.deliveryOrderFailed(orderId);
    }

    @PostMapping("/completed")
    public OrderDto completedOrder(@RequestBody UUID orderId) {
        log.info("Заказа с ID = {} получил статус доставки = COMPLETED.", orderId);
        return orderService.completedOrder(orderId);
    }

    @PostMapping("/calculate/total")
    public OrderDto calculateOrderTotalPrice(@RequestBody UUID orderId) {
        log.info("Начинаем расчет полной стоимости заказа с ID = {}.", orderId);
        return orderService.calculateOrderTotalPrice(orderId);
    }

    @PostMapping("/calculate/delivery")
    public OrderDto calculateOrderDeliveryPrice(@RequestBody UUID orderId) {
        log.info("Начинаем расчет стоимости доставки заказа с ID = {}.", orderId);
        return orderService.calculateOrderDeliveryPrice(orderId);
    }

    @PostMapping("/assembly")
    public OrderDto assemblyOrder(@RequestBody UUID orderId) {
        log.info("Начинаем сборку заказа с ID = {}.", orderId);
        return orderService.assemblyOrder(orderId);
    }

    @PostMapping("/assembly/failed")
    public OrderDto assemblyOrderFailed(@RequestBody UUID orderId) {
        log.info("Сборка заказа c ID {} произошла с ошибкой", orderId);
        return orderService.assemblyOrderFailed(orderId);
    }
}