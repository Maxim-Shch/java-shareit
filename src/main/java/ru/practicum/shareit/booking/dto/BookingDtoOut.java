package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDtoOut {

    private Long id;
    private LocalDateTime start; //дата и время начала бронирования;
    private LocalDateTime end; //дата и время конца бронирования;
    private BookingStatus status; //статус бронирования;
    private UserDto booker;
    private ItemDto item;
}
