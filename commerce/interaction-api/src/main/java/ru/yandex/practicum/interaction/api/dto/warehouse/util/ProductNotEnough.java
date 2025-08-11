package ru.yandex.practicum.interaction.api.dto.warehouse.util;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProductNotEnough {
    UUID productId;
    Integer availableCount;
    Integer wantedCount;
    Integer differenceCount;

    public ProductNotEnough(UUID productId, Integer availableCount, Integer wantedCount) {
        this.productId = productId;
        this.availableCount = availableCount;
        this.wantedCount = wantedCount;
        this.differenceCount = wantedCount - availableCount;
    }
}
