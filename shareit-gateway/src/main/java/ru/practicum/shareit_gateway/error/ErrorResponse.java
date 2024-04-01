package ru.practicum.shareit_gateway.error;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ErrorResponse {

    private final String error;

    public String getError() {
        return error;
    }
}
