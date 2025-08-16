package ru.yandex.practicum.interaction.api.dto.warehouse;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressDto {
    String country;
    String city;
    String street;
    String house;
    String flat;
}