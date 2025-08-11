package ru.yandex.practicum.warehouse.service;

import jakarta.ws.rs.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction.api.dto.warehouse.AddressDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction.api.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.interaction.api.enums.QuantityState;
import ru.yandex.practicum.interaction.api.exception.warehouse.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.interaction.api.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.interaction.api.exception.warehouse.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.interaction.api.feign.client.store.StoreFeignClient;
import ru.yandex.practicum.warehouse.mapper.AddressMapper;
import ru.yandex.practicum.warehouse.mapper.WarehouseProductMapper;
import ru.yandex.practicum.warehouse.model.Address;
import ru.yandex.practicum.warehouse.model.Dimension;
import ru.yandex.practicum.warehouse.model.WarehouseProduct;
import ru.yandex.practicum.warehouse.repository.AddressRepository;
import ru.yandex.practicum.warehouse.repository.WarehouseRepository;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
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
    private final UUID idAddress;
    private final StoreFeignClient storeFeignClient;

    public WarehouseServiceImpl(AddressRepository addressRepository, WarehouseRepository warehouseRepository,
                                AddressMapper addressMapper, WarehouseProductMapper warehouseProductMapper, StoreFeignClient storeFeignClient) {
        this.addressRepository = addressRepository;
        this.warehouseRepository = warehouseRepository;
        this.addressMapper = addressMapper;
        this.warehouseProductMapper = warehouseProductMapper;
        this.storeFeignClient = storeFeignClient;
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
        if (shoppingCartDto == null) {
            throw new IllegalArgumentException("DTO корзины не может быть null");
        }

        Map<UUID, Integer> cartProducts = Optional.ofNullable(shoppingCartDto.getCartProducts())
                .orElseThrow(() -> new BadRequestException("Список продуктов в корзине пуст"));

        Set<UUID> cartProductIds = cartProducts.keySet();
        List<WarehouseProduct> foundProducts = warehouseRepository.findAllById(cartProductIds);


        Set<UUID> foundProductIds = foundProducts.stream()
                .map(WarehouseProduct::getProductId)
                .collect(Collectors.toSet());

        cartProductIds.forEach(id -> {
            if (!foundProductIds.contains(id)) {
                throw new NoSpecifiedProductInWarehouseException(
                        String.format("Товар с ID %s отсутствует на складе", id)
                );
            }
        });

        Map<UUID, WarehouseProduct> warehouseProducts = foundProducts.stream()
                .collect(Collectors.toMap(
                        WarehouseProduct::getProductId,
                        Function.identity(),
                        (existing, replacement) -> existing
                ));

        cartProducts.forEach((productId, requestedQuantity) -> {
            WarehouseProduct product = warehouseProducts.get(productId);
            if (product.getQuantity() < requestedQuantity) {
                throw new ProductInShoppingCartLowQuantityInWarehouse(
                        String.format("Недостаточно товара %s на складе. Доступно: %d, Запрошено: %d",
                                productId, product.getQuantity(), requestedQuantity)
                );
            }
        });

        return getBookedProducts(foundProducts, cartProducts);
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

    private BookedProductsDto getBookedProducts(Collection<WarehouseProduct> productList,
                                                Map<UUID, Integer> cartProducts) {

        return BookedProductsDto.builder()
                .fragile(productList.stream()
                        .anyMatch(p -> Boolean.TRUE.equals(p.getFragile())))
                .deliveryWeight(productList.stream()
                        .mapToDouble(p -> {
                            Double weight = p.getWeight();
                            Integer quantity = cartProducts.get(p.getProductId());
                            return (weight != null ? weight : 0.0) * (quantity != null ? quantity : 0);
                        })
                        .sum())
                .deliveryVolume(productList.stream()
                        .mapToDouble(p -> {
                            Dimension dim = p.getDimension();
                            Integer quantity = cartProducts.get(p.getProductId());
                            return (dim != null ?
                                    dim.getWidth() * dim.getHeight() * dim.getDepth() : 0.0)
                                    * (quantity != null ? quantity : 0);
                        })
                        .sum())
                .build();

    }
}
