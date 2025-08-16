package ru.yandex.practicum.interaction.api.dto.store;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
@Builder
public class ProductPageDto {
    List<ProductDto> content;
    Sort sort;

    public ProductPageDto(List<ProductDto> content, Sort sort) {
        this.content = content;
        this.sort = sort;
    }
}