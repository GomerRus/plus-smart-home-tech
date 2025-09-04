package ru.yandex.practicum.interaction.api.feign.client.order;

import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.interaction.api.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.interaction.api.dto.order.ProductReturnRequest;

import java.util.UUID;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderFeignClient {

    Page<OrderDto> getOrderByUsername(@Valid @RequestParam String username,
                                      @RequestParam @PageableDefault(size = 10,
                                              page = 0,
                                              direction = Sort.Direction.DESC) Pageable pageable) throws FeignException;

    OrderDto createNewOrder(@RequestParam String username,
                            @Valid @RequestBody CreateNewOrderRequest createOrder) throws FeignException;

    OrderDto returnOrderProducts(@Valid @RequestBody ProductReturnRequest productReturn) throws FeignException;


    OrderDto paymentOrder(@RequestBody UUID orderId) throws FeignException;


    OrderDto paymentOrderFailed(@RequestBody UUID orderId) throws FeignException;

    OrderDto deliveryOrder(@RequestBody UUID orderId) throws FeignException;

    OrderDto deliveryOrderFailed(@RequestBody UUID orderId) throws FeignException;

    OrderDto completedOrder(@RequestBody UUID orderId) throws FeignException;

    OrderDto calculateOrderTotalPrice(@RequestBody UUID orderId) throws FeignException;

    OrderDto calculateOrderDeliveryPrice(@RequestBody UUID orderId) throws FeignException;


    OrderDto assemblyOrder(@RequestBody UUID orderId) throws FeignException;


    OrderDto assemblyOrderFailed(@RequestBody UUID orderId) throws FeignException;
}