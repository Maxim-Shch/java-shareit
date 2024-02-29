package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

    private Long id; //— уникальный идентификатор пользователя;
    private String name; //имя или логин пользователя;
    private String email; //email, что два пользователя не могут иметь одинаковый адрес электронной почты);
}
