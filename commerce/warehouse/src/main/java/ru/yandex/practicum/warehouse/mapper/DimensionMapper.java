package ru.yandex.practicum.warehouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.interaction.api.dto.warehouse.DimensionDto;
import ru.yandex.practicum.warehouse.model.Dimension;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DimensionMapper {
    Dimension mapToDimension(DimensionDto dimensionDto);
}