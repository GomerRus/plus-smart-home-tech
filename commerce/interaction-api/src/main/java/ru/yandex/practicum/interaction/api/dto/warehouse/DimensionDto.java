package ru.yandex.practicum.interaction.api.dto.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DimensionDto {
    @Min(1)
    @NotNull(message = "width не может быть NULL.")
    Double width;

    @Min(1)
    @NotNull(message = "height не может быть NULL.")
    Double height;

    @Min(1)
    @NotNull(message = "depth не может быть NULL.")
    Double depth;
}