package ru.yandex.practicum.shopping.store.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.interaction.api.enums.ProductCategory;
import ru.yandex.practicum.shopping.store.model.Product;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByProductCategory(ProductCategory category, Pageable pageable);
}