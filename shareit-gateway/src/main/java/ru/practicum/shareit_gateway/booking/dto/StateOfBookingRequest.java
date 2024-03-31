package ru.practicum.shareit_gateway.booking.dto;

import ru.practicum.shareit_gateway.error.BookingStateException;

import java.util.Optional;

public enum StateOfBookingRequest {

    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static Optional<StateOfBookingRequest> from(String stringState) {
        for (StateOfBookingRequest state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        throw new BookingStateException("Unknown state: " + stringState);
    }
}
