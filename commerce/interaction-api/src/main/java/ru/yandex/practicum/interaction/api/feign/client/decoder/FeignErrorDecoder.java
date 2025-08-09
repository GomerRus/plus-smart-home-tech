package ru.yandex.practicum.interaction.api.feign.client.decoder;

import feign.Response;
import feign.codec.ErrorDecoder;
import ru.yandex.practicum.interaction.api.feign.client.decoder.exception.InternalServerErrorException;
import ru.yandex.practicum.interaction.api.feign.client.decoder.exception.NotFoundException;

public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 404 -> {
                return new NotFoundException("Ресурс NotFound для метода: " + methodKey);
            }
            case 500 -> {
                return new InternalServerErrorException("Произошла EXCEPTION сервера");
            }
            default -> {
                return defaultDecoder.decode(methodKey, response);
            }
        }
    }
}