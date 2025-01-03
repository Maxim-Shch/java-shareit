package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdateDto {

    private String name; //имя или логин пользователя;
    private String email; //email, что два пользователя не могут иметь одинаковый адрес электронной почты);

    public UserUpdateDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
