package ru.yandex.practicum.shopping.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.shopping.cart.model.CartProduct;
import ru.yandex.practicum.shopping.cart.model.CartProductId;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, CartProductId> {
    void deleteAllByCartProductIdCartIdAndCartProductIdProductIdIn(UUID cartId, List<UUID> productIds);
}
