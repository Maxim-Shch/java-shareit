package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    private Long id; //— уникальный идентификатор пользователя;
    private String name; //имя или логин пользователя;
    private String email; //email, что два пользователя не могут иметь одинаковый адрес электронной почты);

    public UserDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
