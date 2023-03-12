package ru.practicum.shareit.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
public class ValidationException extends RuntimeException {
    public ValidationException(final String message, HttpMethod method) {
        log.error(message);

        if (HttpMethod.PUT.equals(method)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (HttpMethod.POST.equals(method)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ValidationException(final String message, HttpMethod method, int status) {
        log.error(message);

        if (HttpMethod.PUT.equals(method)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (HttpMethod.POST.equals(method) & status == 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (HttpMethod.POST.equals(method) & status == 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
