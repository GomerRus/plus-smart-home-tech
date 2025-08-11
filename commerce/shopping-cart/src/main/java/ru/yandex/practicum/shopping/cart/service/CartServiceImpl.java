package ru.yandex.practicum.shopping.cart.service;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.api.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.interaction.api.exception.cart.NoProductsInShoppingCartException;
import ru.yandex.practicum.interaction.api.exception.cart.NotAuthorizedUserException;
import ru.yandex.practicum.interaction.api.exception.cart.ShoppingCartDeactivateException;
import ru.yandex.practicum.interaction.api.feign.client.warehouse.WarehouseFeignClient;
import ru.yandex.practicum.shopping.cart.mapper.ShoppingCartMapper;
import ru.yandex.practicum.shopping.cart.model.ShoppingCart;
import ru.yandex.practicum.shopping.cart.model.enums.ShoppingCartStatus;
import ru.yandex.practicum.shopping.cart.repository.ShoppingCartRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper mapper;
    private final WarehouseFeignClient warehouseFeignClient;

    private void checkUsernameForEmpty(String username) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException("Username is empty");
        }
    }

    private ShoppingCart getOrCreateCart(String username) {
        return shoppingCartRepository.findByUsername(username)
                .orElseGet(() -> {

                    ShoppingCart cart = ShoppingCart.builder()
                            .username(username)
                            .build();
                    shoppingCartRepository.save(cart);

                    return cart;
                });
    }

    private void validateCartStatus(ShoppingCart cart) {
        if (cart == null) {
            throw new NotFoundException("Корзина не найдена");
        }

        if (cart.getStatus() == null) {
            throw new IllegalStateException("Статус корзины не определен");
        }

        if (cart.getStatus().equals(ShoppingCartStatus.DEACTIVATE)) {
            throw new ShoppingCartDeactivateException(("Корзина пользователя деактивирована")
            );
        }
    }

    private void checkAvailableProductsInWarehouse(UUID shoppingCartId, Map<UUID, Integer> products) {
        ShoppingCartDto shoppingCartDto = ShoppingCartDto.builder()
                .cartId(shoppingCartId)
                .products(products)
                .build();

        warehouseFeignClient.checkQuantityProducts(shoppingCartDto);

    }

    private void validateCartHaveAllProduct(ShoppingCart shoppingCart, Collection<UUID> productsIds) {
        int countProductInCart = shoppingCart.getProducts().size();
        int countProductToCheck = productsIds.size();

        if (countProductToCheck > countProductInCart) {
            throw new NoProductsInShoppingCartException("Количество проверяемых товаров больше чем товаров в корзине");
        }

        List<UUID> notFoundIds = new ArrayList<>();

        productsIds.forEach(id -> {
            if (!shoppingCart.getProducts().containsKey(id)) {
                notFoundIds.add(id);
            }
        });

        if (!notFoundIds.isEmpty()) {
            throw new NoProductsInShoppingCartException("Обнаружены товары, которых нет в корзине.");
        }
    }

    @Override
    @Transactional
    public ShoppingCartDto getShoppingCart(String username) {
        checkUsernameForEmpty(username);
        ShoppingCart cart = getOrCreateCart(username);

        return mapper.mapToCartDto(cart);
    }

    @Override
    @Transactional
    public ShoppingCartDto addProductInCart(String username, Map<UUID, Integer> products) {
        checkUsernameForEmpty(username);

        if (products == null || products.isEmpty()) {
            throw new BadRequestException("Список продуктов не может быть пустым");
        }

        ShoppingCart cart = getOrCreateCart(username);
        validateCartStatus(cart);

        checkAvailableProductsInWarehouse(cart.getCartId(), products);

        products.forEach((productId, quantity) -> cart.getProducts().merge(productId,
                quantity, Integer::sum));
        return mapper.mapToCartDto(cart);
    }

    @Override
    @Transactional
    public void deactivationShoppingCart(String username) {
        checkUsernameForEmpty(username);
        ShoppingCart cart = getOrCreateCart(username);
        cart.setStatus(ShoppingCartStatus.DEACTIVATE);
    }

    @Override
    @Transactional
    public ShoppingCartDto removeProductFromCart(String username, List<UUID> productsIds) {
        checkUsernameForEmpty(username);
        ShoppingCart cart = getOrCreateCart(username);
        validateCartStatus(cart);
        validateCartHaveAllProduct(cart, productsIds);
        productsIds.forEach(id -> cart.getProducts().remove(id));

        return mapper.mapToCartDto(cart);
    }

    @Override
    @Transactional
    public ShoppingCartDto changeQuantityInCart(String username, ChangeProductQuantityRequest quantityRequest) {
        checkUsernameForEmpty(username);

        if (quantityRequest == null) {
            throw new BadRequestException("Запрос на изменение количества не может быть пустым");
        }

        if (quantityRequest.getProductId() == null || quantityRequest.getNewQuantity() == null) {
            throw new BadRequestException("productId и newQuantity должны быть заполнены");
        }

        ShoppingCart cart = getOrCreateCart(username);

        validateCartStatus(cart);
        validateCartHaveAllProduct(cart, List.of(quantityRequest.getProductId()));
        checkAvailableProductsInWarehouse(cart.getCartId(),
                Map.of(quantityRequest.getProductId(), quantityRequest.getNewQuantity()));

        cart.getProducts().forEach((id, count) -> cart.getProducts().put(id, count));

        return mapper.mapToCartDto(cart);
    }
}