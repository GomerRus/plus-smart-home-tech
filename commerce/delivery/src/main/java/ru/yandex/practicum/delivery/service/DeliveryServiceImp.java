package ru.yandex.practicum.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.delivery.mapper.DeliveryMapper;
import ru.yandex.practicum.delivery.model.Address;
import ru.yandex.practicum.delivery.model.Delivery;
import ru.yandex.practicum.delivery.repository.DeliveryRepository;
import ru.yandex.practicum.interaction.api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.interaction.api.enums.DeliveryState;
import ru.yandex.practicum.interaction.api.exception.delivery.NoDeliveryFoundException;
import ru.yandex.practicum.interaction.api.feign.client.order.OrderFeignClient;
import ru.yandex.practicum.interaction.api.feign.client.warehouse.WarehouseFeignClient;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryServiceImp implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final WarehouseFeignClient warehouseClient;
    private final OrderFeignClient orderClient;

    @Value("${delivery.base.cost}")
    private Double baseCost;

    private Delivery getDeliveryById(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NoDeliveryFoundException(
                        String.format("ОШИБКА: Доставка с ID = %s не найдена", deliveryId)));
    }

    @Override
    @Transactional
    public DeliveryDto createNewDelivery(DeliveryDto deliveryDto) {
        return deliveryMapper.mapToDeliveryDto(deliveryRepository.save(deliveryMapper.mapToDelivery(deliveryDto)));
    }

    @Override
    @Transactional
    public void successfulDelivery(UUID deliveryId) {
        Delivery delivery = getDeliveryById(deliveryId);
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        orderClient.deliveryOrder(delivery.getOrderID());
    }

    @Override
    @Transactional
    public void pickedProductsInDelivery(UUID deliveryId) {
        Delivery delivery = getDeliveryById(deliveryId);
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);

        warehouseClient.shippedProductForDelivery(ShippedToDeliveryRequest.builder()
                .orderId(delivery.getOrderID())
                .deliveryId(deliveryId)
                .build());
    }

    @Override
    @Transactional
    public void failedDelivery(UUID deliveryId) {
        Delivery delivery = getDeliveryById(deliveryId);
        delivery.setDeliveryState(DeliveryState.FAILED);
        orderClient.deliveryOrderFailed(delivery.getOrderID());
    }

    @Override
    public Double coastDelivery(OrderDto orderDto) {
        Delivery delivery = getDeliveryById(orderDto.getDeliveryId());

        Address fromAddress = delivery.getFromAddress();
        Address toAddress = delivery.getToAddress();

        double warehouseMarKup = 1.0;
        if (fromAddress.toString().contains("ADDRESS_1")) {
            warehouseMarKup = 1.0;
        } else if (fromAddress.toString().contains("ADDRESS_2")) {
            warehouseMarKup = 2.0;
        }
        double deliveryCost = baseCost + (baseCost * warehouseMarKup);

        if (orderDto.getFragile()) {
            deliveryCost += deliveryCost * 0.2;
        }
        deliveryCost += orderDto.getDeliveryWeight() * 0.3 + orderDto.getDeliveryVolume() * 0.2;

        if (fromAddress.getStreet() != null && toAddress.getStreet() != null) {
            String fromStreet = fromAddress.getStreet().trim().toLowerCase();
            String toStreet = toAddress.getStreet().trim().toLowerCase();
            if (!fromStreet.equals(toStreet)) {
                deliveryCost += deliveryCost * 0.2;
            }
        } else {
            throw new IllegalArgumentException("Адрес не может быть null");
        }
        return deliveryCost;
    }
}
