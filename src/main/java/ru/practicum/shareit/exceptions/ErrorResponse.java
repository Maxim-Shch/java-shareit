package ru.practicum.shareit.exceptions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ErrorResponse {

    private final String error;

    public String getError() {
        return error;
    }
}

