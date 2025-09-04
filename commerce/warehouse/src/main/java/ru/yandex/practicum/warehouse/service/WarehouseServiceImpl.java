package ru.yandex.practicum.warehouse.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction.api.dto.warehouse.AddressDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.interaction.api.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.interaction.api.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.interaction.api.dto.warehouse.util.ProductNotEnough;
import ru.yandex.practicum.interaction.api.exception.warehouse.MultipleProductsNotFoundException;
import ru.yandex.practicum.interaction.api.exception.warehouse.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.interaction.api.exception.warehouse.OrderBookingNotFoundException;
import ru.yandex.practicum.interaction.api.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.interaction.api.exception.warehouse.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.warehouse.mapper.AddressMapper;
import ru.yandex.practicum.warehouse.mapper.WarehouseProductMapper;
import ru.yandex.practicum.warehouse.model.Address;
import ru.yandex.practicum.warehouse.model.Dimension;
import ru.yandex.practicum.warehouse.model.OrderBooking;
import ru.yandex.practicum.warehouse.model.WarehouseProduct;
import ru.yandex.practicum.warehouse.repository.AddressRepository;
import ru.yandex.practicum.warehouse.repository.OrderBookingRepository;
import ru.yandex.practicum.warehouse.repository.WarehouseRepository;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class WarehouseServiceImpl implements WarehouseService {
    private final AddressRepository addressRepository;
    private final WarehouseRepository warehouseRepository;
    private final AddressMapper addressMapper;
    private final WarehouseProductMapper warehouseProductMapper;
    private final OrderBookingRepository orderBookingRepository;
    private final UUID idAddress;

    public WarehouseServiceImpl(AddressRepository addressRepository, WarehouseRepository warehouseRepository,
                                AddressMapper addressMapper, WarehouseProductMapper warehouseProductMapper,
                                OrderBookingRepository orderBookingRepository) {
        this.addressRepository = addressRepository;
        this.warehouseRepository = warehouseRepository;
        this.addressMapper = addressMapper;
        this.warehouseProductMapper = warehouseProductMapper;
        this.orderBookingRepository = orderBookingRepository;
        String[] address = {"ADDRESS_1", "ADDRESS_2"};
        int randomIdx = Random.from(new SecureRandom()).nextInt(0, address.length);
        this.idAddress = addressRepository.save(Address.createAddress(address[randomIdx])).getId();
    }


    @Override
    @Transactional
    public void newProduct(NewProductInWarehouseRequest newRequest) {
        if (warehouseRepository.existsById(newRequest.getProductId())) {
            throw new SpecifiedProductAlreadyInWarehouseException("Товар с ID = "
                    + newRequest.getProductId() + "уже зарегистрирован.");
        }

        WarehouseProduct product = warehouseProductMapper.mapToWarProduct(newRequest);
        warehouseRepository.save(product);
    }

    @Override
    public BookedProductsDto checkQuantityProducts(ShoppingCartDto shoppingCartDto) {
        Map<UUID, WarehouseProduct> warehouseProducts = warehouseRepository
                .findAllById(shoppingCartDto.getProducts().keySet()).stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, Function.identity()));

        return checkQuantityProductsImpl(shoppingCartDto.getProducts(), warehouseProducts);
    }

    private BookedProductsDto checkQuantityProductsImpl(Map<UUID, Integer> cartProducts,
                                                        Map<UUID, WarehouseProduct> warehouseProducts) {

        BookedProductsDto result = BookedProductsDto.builder()
                .deliveryVolume(0.0)
                .deliveryWeight(0.0)
                .fragile(false)
                .build();

        List<ProductNotEnough> productsNotEnough = new ArrayList<>();
        List<UUID> productsNotFound = new ArrayList<>();

        for (Map.Entry<UUID, Integer> entry : cartProducts.entrySet()) {
            UUID id = entry.getKey();
            Integer wantedCount = entry.getValue();

            if (!warehouseProducts.containsKey(id)) {
                productsNotFound.add(id);
                continue;
            }

            WarehouseProduct product = warehouseProducts.get(id);
            Integer availableCount = product.getQuantity();

            if (wantedCount > availableCount) {
                productsNotEnough.add(new ProductNotEnough(id, availableCount, wantedCount));
                continue;
            }

            Dimension dimension = product.getDimension();

            Double currentVolume = result.getDeliveryVolume();
            Double addVolume = dimension.getHeight() * dimension.getWidth() * dimension.getDepth();
            Double newVolume = currentVolume + addVolume;
            result.setDeliveryVolume(newVolume);

            Double currentWeight = result.getDeliveryWeight();
            Double addWeight = product.getWeight() * wantedCount;
            Double newWeight = currentWeight + addWeight;
            result.setDeliveryWeight(newWeight);

            if (product.getFragile() != null) {
                boolean fragile = result.getFragile() || product.getFragile();
                result.setFragile(fragile);
            }
        }

        if (!productsNotFound.isEmpty()) {
            throw new NoSpecifiedProductInWarehouseException("Нет информации о товарах на складе ID: " + productsNotFound);
        }

        if (!productsNotEnough.isEmpty()) {
            throw new ProductInShoppingCartLowQuantityInWarehouse("Недостаточно товаров на складе: " + productsNotEnough);
        }

        return result;
    }

    @Override
    @Transactional
    public void addQuantityProduct(AddProductToWarehouseRequest addRequest) {
        WarehouseProduct product = warehouseRepository.findById(addRequest.getProductId())
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException
                        (String.format("Товара с ID = %s нeт на складе", addRequest.getProductId())));
        product.setQuantity(product.getQuantity() + addRequest.getQuantity());
    }

    @Override
    public AddressDto getAddress() {
        Address address = addressRepository.findById(idAddress)
                .orElseThrow(() -> new IllegalStateException("Адрес в БД не найден, ID = " + idAddress));
        return addressMapper.mapToAddressDto(address);
    }

    @Override
    @Transactional
    public void shippedProductForDelivery(ShippedToDeliveryRequest shippedRequest) {
        OrderBooking orderBooking = orderBookingRepository.findById(shippedRequest.getOrderId())
                .orElseThrow(() -> new OrderBookingNotFoundException(
                        String.format("Для заказа с ID %s бронирование не найдено",shippedRequest.getOrderId())));

        orderBooking.setDeliveryId(shippedRequest.getDeliveryId());
    }

    @Override
    @Transactional
    public void returnProductToTheWarehouse(Map<UUID, Integer> products) {
        Map<UUID, WarehouseProduct> warehouseProducts = warehouseRepository.findAllById(products.keySet())
                .stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, Function.identity()));

        List<UUID> missingIds = new ArrayList<>();

        products.forEach((productId, quantity) -> {
            if (!warehouseProducts.containsKey(productId)) {
                missingIds.add(productId);
            } else {
                WarehouseProduct warehouseProduct = warehouseProducts.get(productId);
                warehouseProduct.setQuantity(warehouseProduct.getQuantity() + quantity);
            }
        });

        if (!missingIds.isEmpty()) {
            String errorMessage = "На складе отсутствуют товары: " +
                    missingIds.stream()
                            .map(UUID::toString)
                            .collect(Collectors.joining(", "));

            throw new MultipleProductsNotFoundException(errorMessage, missingIds);
        }
    }

    @Override
    @Transactional
    public BookedProductsDto assemblyProductOnOrderForDelivery(AssemblyProductsForOrderRequest assemblyRequest) {
        Map<UUID, Integer> assemblyProducts = assemblyRequest.getProducts();

        Map<UUID, WarehouseProduct> warehouseProducts = warehouseRepository
                .findAllById(assemblyProducts.keySet()).stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, Function.identity()));

        BookedProductsDto bookedProducts = checkQuantityProductsImpl(assemblyProducts, warehouseProducts);

        warehouseProducts.forEach((key, value) -> value.setQuantity(value.getQuantity() - assemblyProducts.get(key)));

        orderBookingRepository.save(OrderBooking.builder()
                .products(assemblyProducts)
                .orderId(assemblyRequest.getOrderId())
                .build());

        return bookedProducts;
    }
}