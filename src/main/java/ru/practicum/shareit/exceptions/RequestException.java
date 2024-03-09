package ru.practicum.shareit.exceptions;

import org.springframework.web.bind.annotation.RequestParam;

public class RequestException extends RuntimeException {
    public RequestException(String message) {
        super(message);
    }
}
