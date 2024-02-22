package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
    private Integer ownerId; //владелец вещи;
    private Integer requestId; //если вещь была создана по запросу другого пользователя,
    // то в этом поле будет храниться ссылка на соответствующий запрос.
}
