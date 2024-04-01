package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.util.List;

@Data
@Builder
public class ItemDto {

    private Long id; //уникальный идентификатор вещи;
    private String name; //краткое наименование вещи;
    private String description; //развёрнутое описание;
    private Boolean available; //статус о том, доступна или нет вещь для аренды;
    // то в этом поле будет храниться ссылка на соответствующий запрос.
    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;
    private List<CommentShortDto> comments;
    private Long requestId;
}
