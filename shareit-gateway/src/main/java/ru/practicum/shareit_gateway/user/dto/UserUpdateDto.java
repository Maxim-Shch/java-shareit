package ru.practicum.shareit_gateway.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@Builder
public class UserUpdateDto {

    private String name; //имя или логин пользователя;
    @Email
    private String email; //email, что два пользователя не могут иметь одинаковый адрес электронной почты);

    public UserUpdateDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
