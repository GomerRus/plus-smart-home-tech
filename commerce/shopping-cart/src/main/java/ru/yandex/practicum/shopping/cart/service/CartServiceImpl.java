package ru.yandex.practicum.shopping.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.api.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.interaction.api.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.interaction.api.dto.store.ProductDto;
import ru.yandex.practicum.interaction.api.exception.cart.CartNotFoundException;
import ru.yandex.practicum.interaction.api.exception.cart.NoProductsInShoppingCartException;
import ru.yandex.practicum.interaction.api.exception.cart.NotAuthorizedUserException;
import ru.yandex.practicum.interaction.api.feign.client.store.StoreFeignClient;
import ru.yandex.practicum.interaction.api.feign.client.warehouse.WarehouseFeignClient;
import ru.yandex.practicum.shopping.cart.mapper.ShoppingCartMapper;
import ru.yandex.practicum.shopping.cart.model.CartProduct;
import ru.yandex.practicum.shopping.cart.model.CartProductId;
import ru.yandex.practicum.shopping.cart.model.ShoppingCart;
import ru.yandex.practicum.shopping.cart.model.User;
import ru.yandex.practicum.shopping.cart.repository.CartProductRepository;
import ru.yandex.practicum.shopping.cart.repository.ShoppingCartRepository;
import ru.yandex.practicum.shopping.cart.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final CartProductRepository cartProductRepository;
    private final ShoppingCartMapper mapper;
    private final StoreFeignClient storeFeignClient;
    private final WarehouseFeignClient warehouseFeignClient;

    private User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new NotAuthorizedUserException(userName));
    }

    private void checkUsernameForEmpty(String userName) {
        if (userName == null || userName.isBlank()) {
            throw new NotAuthorizedUserException("Username is empty");
        }
    }

    private ShoppingCart getActiveCart(String userName) {
        return shoppingCartRepository.findByUserUserNameAndIsActiveTrue(userName)
                .orElseThrow(() -> new CartNotFoundException(userName));
    }

    private ShoppingCart createNewCart(User user) {
        ShoppingCart newCart = new ShoppingCart();
        newCart.setUser(user);
        newCart.setIsActive(true);
        return shoppingCartRepository.save(newCart);
    }

    private void checkAvailableProductsInWarehouse(UUID shoppingCartId, Map<UUID, Integer> products) {
        ShoppingCartDto shoppingCartDto = ShoppingCartDto.builder()
                .shoppingCartId(shoppingCartId)
                .products(products)
                .build();

        warehouseFeignClient.checkQuantityProducts(shoppingCartDto);

    }

    @Override
    @Transactional
    public ShoppingCartDto getShoppingCart(String userName) {
        User user = getUserByUserName(userName);
        ShoppingCart cart = shoppingCartRepository.findByUserUserNameAndIsActiveTrue(userName)
                .orElseGet(() -> createNewCart(user));

        return mapper.mapToCartDto(cart);
    }

    @Override
    @Transactional
    public ShoppingCartDto addProductInCart(String userName, Map<UUID, Integer> products) {
        checkUsernameForEmpty(userName);
        ShoppingCart cart = getActiveCart(userName);
        checkAvailableProductsInWarehouse(cart.getCartId(), products);

        products.forEach((productId, quantity) -> {
            ProductDto productDto = storeFeignClient.getProductById(productId);
            if (productDto == null) {
                throw new NoProductsInShoppingCartException(productId);
            }

            cart.getCartProducts().stream()
                    .filter(cp -> cp.getCartProductId().getProductId().equals(productId))
                    .findFirst()
                    .ifPresentOrElse(
                            cp -> cp.setQuantity(cp.getQuantity() + quantity),
                            () -> {
                                CartProductId id = CartProductId.builder()
                                        .cartId(cart.getCartId())
                                        .productId(productId)
                                        .build();

                                CartProduct newCartProduct = CartProduct.builder()
                                        .cartProductId(id)
                                        .quantity(quantity)
                                        .build();
                                cart.getCartProducts().add(newCartProduct);
                            }
                    );
        });

        return mapper.mapToCartDto(shoppingCartRepository.save(cart));
    }

    @Override
    @Transactional
    public ShoppingCartDto deactivationShoppingCart(String userName) {
        checkUsernameForEmpty(userName);
        ShoppingCart cart = getActiveCart(userName);
        cart.setIsActive(false);
        return mapper.mapToCartDto(shoppingCartRepository.save(cart));
    }

    @Override
    @Transactional
    public ShoppingCartDto removeProductFromCart(String userName, List<UUID> productsIds) {
        checkUsernameForEmpty(userName);
        ShoppingCart cart = getActiveCart(userName);
        cartProductRepository.deleteAllByCartProductIdCartIdAndCartProductIdProductIdIn(cart.getCartId(), productsIds);
        cart.getCartProducts().removeIf(cp -> productsIds.contains(cp.getCartProductId().getProductId()));
        return mapper.mapToCartDto(shoppingCartRepository.save(cart));
    }

    @Override
    @Transactional
    public ShoppingCartDto changeQuantityInCart(String userName, ChangeProductQuantityRequest quantityRequest) {
        checkUsernameForEmpty(userName);
        ShoppingCart cart = getActiveCart(userName);
        checkAvailableProductsInWarehouse(cart.getCartId(),
                Map.of(quantityRequest.getProductId(), quantityRequest.getNewQuantity()));

        cart.getCartProducts().stream()
                .filter(cp -> cp.getCartProductId().getProductId().equals(quantityRequest.getProductId()))
                .findFirst()
                .ifPresentOrElse(
                        cp -> cp.setQuantity(quantityRequest.getNewQuantity()),
                        () -> {
                            throw new NoProductsInShoppingCartException(quantityRequest.getProductId());
                        }
                );

        return mapper.mapToCartDto(shoppingCartRepository.save(cart));

    }
}
