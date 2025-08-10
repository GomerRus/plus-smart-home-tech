package ru.yandex.practicum.shopping.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.api.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.interaction.api.exception.cart.CartNotFoundException;
import ru.yandex.practicum.interaction.api.exception.cart.NoProductsInShoppingCartException;
import ru.yandex.practicum.interaction.api.exception.cart.NotAuthorizedUserException;
import ru.yandex.practicum.interaction.api.feign.client.warehouse.WarehouseFeignClient;
import ru.yandex.practicum.shopping.cart.mapper.ShoppingCartMapper;
import ru.yandex.practicum.shopping.cart.model.ShoppingCart;
import ru.yandex.practicum.shopping.cart.model.enums.ShoppingCartStatus;
import ru.yandex.practicum.shopping.cart.repository.ShoppingCartRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

    private ShoppingCart getActiveCart(String username) {
        return shoppingCartRepository.findByUsernameAndStatus(username, ShoppingCartStatus.ACTIVE)
                .orElseThrow(() -> new CartNotFoundException
                        (String.format("У пользователя %s нет активной корзины.", username)));
    }

    private ShoppingCart createNewCart(String username) {
        ShoppingCart newCart = new ShoppingCart();
        newCart.setUsername(username);
        newCart.setStatus(ShoppingCartStatus.ACTIVE);
        newCart.setCartProducts(new HashMap<>());
        return shoppingCartRepository.save(newCart);
    }

    private void checkAvailableProductsInWarehouse(UUID shoppingCartId, Map<UUID, Integer> products) {
        ShoppingCartDto shoppingCartDto = ShoppingCartDto.builder()
                .cartId(shoppingCartId)
                .cartProducts(products)
                .build();

        warehouseFeignClient.checkQuantityProducts(shoppingCartDto);

    }

    @Override
    @Transactional
    public ShoppingCartDto getShoppingCart(String username) {
        checkUsernameForEmpty(username);
        ShoppingCart cart = shoppingCartRepository.findByUsernameAndStatus(username, ShoppingCartStatus.ACTIVE)
                .orElseGet(() -> createNewCart(username));

        return mapper.mapToCartDto(cart);
    }

    @Override
    @Transactional
    public ShoppingCartDto addProductInCart(String username, Map<UUID, Integer> products) {
        checkUsernameForEmpty(username);
        ShoppingCart cart = getActiveCart(username);
        checkAvailableProductsInWarehouse(cart.getCartId(), products);

        products.forEach((productId, quantity) -> cart.getCartProducts().merge(productId, quantity, Integer::sum));

        return mapper.mapToCartDto(cart);
    }

    @Override
    @Transactional
    public ShoppingCartDto deactivationShoppingCart(String username) {
        checkUsernameForEmpty(username);
        ShoppingCart cart = getActiveCart(username);
        cart.setStatus(ShoppingCartStatus.DEACTIVATE);
        return mapper.mapToCartDto(cart);
    }

    @Override
    @Transactional
    public ShoppingCartDto removeProductFromCart(String username, List<UUID> productsIds) {
        checkUsernameForEmpty(username);
        ShoppingCart cart = getActiveCart(username);
        Map<UUID, Integer> oldProducts = cart.getCartProducts();

        if (!productsIds.stream().allMatch(oldProducts::containsKey)) {
            throw new NoProductsInShoppingCartException("Указанных продуктов нет в корзине");
        }

        Map<UUID, Integer> newProducts = oldProducts.entrySet().stream()
                .filter(cp -> !productsIds.contains(cp.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        cart.setCartProducts(newProducts);
        return mapper.mapToCartDto(cart);
    }

    @Override
    @Transactional
    public ShoppingCartDto changeQuantityInCart(String username, ChangeProductQuantityRequest quantityRequest) {
        checkUsernameForEmpty(username);
        ShoppingCart cart = getActiveCart(username);
        checkAvailableProductsInWarehouse(cart.getCartId(),
                Map.of(quantityRequest.getProductId(), quantityRequest.getNewQuantity()));

        Map<UUID, Integer> products = cart.getCartProducts();

        if (!products.containsKey(quantityRequest.getProductId())) {
            throw new NoProductsInShoppingCartException("Указанного продукта нет в корзине");
        }

        products.put(quantityRequest.getProductId(), quantityRequest.getNewQuantity());

        return mapper.mapToCartDto(cart);

    }
}
