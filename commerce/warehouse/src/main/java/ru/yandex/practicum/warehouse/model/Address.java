package ru.yandex.practicum.warehouse.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "address")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {
    @Id
    @UuidGenerator
    @Column(name = "id")
    UUID id;

    @Column(nullable = false)
    String country;

    @Column(nullable = false)
    String city;

    @Column(nullable = false)
    String street;

    @Column(nullable = false)
    String house;

    String flat;

    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL)
    List<WarehouseProduct> warehouseProducts;

    public static Address createAddress(String value) {
        return Address.builder()
                .country(value)
                .city(value)
                .street(value)
                .house(value)
                .flat(value)
                .build();
    }
}





















