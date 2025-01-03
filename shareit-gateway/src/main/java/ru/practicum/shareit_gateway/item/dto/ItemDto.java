package ru.practicum.shareit_gateway.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit_gateway.booking.dto.BookingShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class ItemDto {

    private Long id; //уникальный идентификатор вещи;
    @NotNull
    @NotEmpty
    @NotBlank
    private String name; //краткое наименование вещи;
    @NotNull
    @NotEmpty
    @NotBlank
    private String description; //развёрнутое описание;
    @NotNull
    private Boolean available; //статус о том, доступна или нет вещь для аренды;
    // то в этом поле будет храниться ссылка на соответствующий запрос.
    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;
    private List<CommentShortDto> comments;
    private Long requestId;
}
