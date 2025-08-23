package ru.yandex.practicum.interaction.api.feign.client.delivery;

import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.interaction.api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction.api.dto.order.OrderDto;

import java.util.UUID;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryFeignClient {

    DeliveryDto createNewDelivery(@Valid @RequestBody DeliveryDto deliveryDto) throws FeignException;

    void successfulDelivery(@Valid @RequestBody UUID deliveryId) throws FeignException;

    void pickedProductsInDelivery(@Valid @RequestBody UUID deliveryId) throws FeignException;

    void failedDelivery(@Valid @RequestBody UUID deliveryId) throws FeignException;

    Double coastDelivery(@Valid @RequestBody OrderDto orderDto) throws FeignException;
}