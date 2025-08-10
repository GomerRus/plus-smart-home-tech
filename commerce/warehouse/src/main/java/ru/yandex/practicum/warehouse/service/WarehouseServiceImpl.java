package ru.yandex.practicum.warehouse.service;

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
import ru.yandex.practicum.warehouse.model.WarehouseProduct;
import ru.yandex.practicum.warehouse.repository.AddressRepository;
import ru.yandex.practicum.warehouse.repository.WarehouseRepository;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.Map;
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

    public WarehouseServiceImpl(AddressRepository addressRepository, WarehouseRepository warehouseRepository,
                                AddressMapper addressMapper, WarehouseProductMapper warehouseProductMapper) {
        this.addressRepository = addressRepository;
        this.warehouseRepository = warehouseRepository;
        this.addressMapper = addressMapper;
        this.warehouseProductMapper = warehouseProductMapper;
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
        Map<UUID, Integer> cartProducts = shoppingCartDto.getCartProducts();
        Set<UUID> cartProductIds = cartProducts.keySet();

        Map<UUID, WarehouseProduct> warehouseProducts = warehouseRepository.findAllById(cartProductIds).stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, Function.identity()));

        Set<UUID> productIds = warehouseProducts.keySet();
        cartProductIds.forEach(id -> {
            if (!productIds.contains(id)) {
                throw new NoSpecifiedProductInWarehouseException(String.format("Товара с ID = %s нет на складе", id));
            }
        });

        cartProducts.forEach((key, value) -> {
            if (warehouseProducts.get(key).getQuantity() < value) {
                throw new ProductInShoppingCartLowQuantityInWarehouse
                        (String.format("Товара с ID = %s не хватает на складе", key));
            }
        });

        return getBookedProducts(warehouseProducts.values(), cartProducts);
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
                .fragile(productList.stream().anyMatch(WarehouseProduct::getFragile))
                .deliveryWeight(productList.stream()
                        .mapToDouble(p -> p.getWeight() * cartProducts.get(p.getProductId()))
                        .sum())
                .deliveryVolume(productList.stream()
                        .mapToDouble(p ->
                                p.getDimension().getWidth()
                                        * p.getDimension().getHeight()
                                        * p.getDimension().getDepth()
                                        * cartProducts.get(p.getProductId()))
                        .sum())
                .build();
    }
}
