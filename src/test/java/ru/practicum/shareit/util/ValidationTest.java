package ru.practicum.shareit.util;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidationTest {
    @Test
    void validationExceptionPost() {
        String error = "test";

        try {
            throw new ValidationException(error, HttpMethod.POST);
        } catch (ResponseStatusException ex) {
            assertEquals(ex.getStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Test
    void validationExceptionPut() {
        String error = "test";

        try {
            throw new ValidationException(error, HttpMethod.PUT);
        } catch (ResponseStatusException ex) {
            assertEquals(ex.getStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Test
    void validationExceptionPutStatus() {
        String error = "test";

        try {
            throw new ValidationException(error, HttpMethod.PUT, 0);
        } catch (ResponseStatusException ex) {
            assertEquals(ex.getStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Test
    void validationExceptionPostStatus() {
        String error = "test";

        try {
            throw new ValidationException(error, HttpMethod.POST, 0);
        } catch (ResponseStatusException ex) {
            assertEquals(ex.getStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Test
    void validationExceptionPost1Status() {
        String error = "test";

        try {
            throw new ValidationException(error, HttpMethod.POST, 1);
        } catch (ResponseStatusException ex) {
            assertEquals(ex.getStatus(), HttpStatus.BAD_REQUEST);
        }
    }

    @Test
    void validationExceptionGetStatus() {
        String error = "test";

        try {
            throw new ValidationException(error, HttpMethod.GET, 1);
        } catch (Error ex) {
            assertEquals(ex.toString(), "java.lang.Error: test");
        }
    }
}
