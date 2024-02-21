package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Data
@Builder
public class Item {

    private Long id; //уникальный идентификатор вещи;
    private String name; //краткое наименование вещи;
    private String description; //развёрнутое описание;
    private Boolean available; //статус о том, доступна или нет вещь для аренды;
    private User owner; //владелец вещи;
    private ItemRequest request;
    //если вещь была создана по запросу другого пользователя,
    //то в этом поле будет храниться ссылка на соответствующий запрос.
}
