package ru.yandex.practicum.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.api.dto.order.OrderDto;
import ru.yandex.practicum.interaction.api.dto.payment.PaymentDto;
import ru.yandex.practicum.interaction.api.dto.store.ProductDto;
import ru.yandex.practicum.interaction.api.enums.PaymentState;
import ru.yandex.practicum.interaction.api.exception.payment.NoPaymentFoundException;
import ru.yandex.practicum.interaction.api.exception.payment.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.interaction.api.feign.client.order.OrderFeignClient;
import ru.yandex.practicum.interaction.api.feign.client.store.StoreFeignClient;
import ru.yandex.practicum.payment.mapper.PaymentMapper;
import ru.yandex.practicum.payment.model.Payment;
import ru.yandex.practicum.payment.repository.PaymentRepository;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final StoreFeignClient storeClient;
    private final OrderFeignClient orderClient;

    @Value("${payment.fee.tax}")
    private Double feeTax;

    private Payment getPaymentById(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoPaymentFoundException(
                        String.format("Платеж с ID = %s не найден.", paymentId)));
    }

    @Override
    @Transactional
    public PaymentDto makingPaymentForOrder(OrderDto orderDto) {
        Payment payment = Payment.builder()
                .orderId(orderDto.getOrderId())
                .totalPayment(calculateTotalCostPayment(orderDto))
                .deliveryTotal(orderDto.getDeliveryPrice())
                .feeTotal(calculateProductCostPayment(orderDto) * feeTax)
                .state(PaymentState.PENDING)
                .build();
        return paymentMapper.mapToPaymentDto(paymentRepository.save(payment));
    }

    @Override
    public Double calculateTotalCostPayment(OrderDto orderDto) {
        Double productPrice = orderDto.getProductPrice();
        if (productPrice == null || orderDto.getDeliveryPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException(
                    String.format("ОШИБКА: Стоимость заказа с ID = %s не возможно рассчитать. Одно из значений %f или %f = 0",
                            orderDto.getOrderId(), productPrice, orderDto.getDeliveryPrice()));
        }

        return productPrice + productPrice * feeTax + orderDto.getDeliveryPrice();
    }

    @Override
    @Transactional
    public void successfulPayment(UUID paymentId) {
        Payment payment = getPaymentById(paymentId);
        payment.setState(PaymentState.SUCCESS);
        orderClient.paymentOrder(payment.getOrderId());
    }

    @Override
    public Double calculateProductCostPayment(OrderDto orderDto) {

        Map<UUID, Integer> products = orderDto.getProducts();

        Map<UUID, Float> price = products.keySet().stream()
                .map(storeClient::getProductById)
                .collect(Collectors.toMap(ProductDto::getProductId, ProductDto::getPrice));

        return products.entrySet().stream()
                .map(entry -> entry.getValue() * price.get(entry.getKey()))
                .mapToDouble(Float::floatValue)
                .sum();
    }

    @Override
    @Transactional
    public void failedPayment(UUID paymentId) {
        Payment payment = getPaymentById(paymentId);
        payment.setState(PaymentState.FAILED);
        orderClient.paymentOrderFailed(payment.getOrderId());
    }
}
