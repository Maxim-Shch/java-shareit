package ru.practicum.shareit_gateway.item.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemUpdateDto {

    private String name; //краткое наименование вещи;
    private String description; //развёрнутое описание;
    private Boolean available; //статус о том, доступна или нет вещь для аренды;
    private Integer requestId; //если вещь была создана по запросу другого пользователя,
    // то в этом поле будет храниться ссылка на соответствующий запрос.
}
