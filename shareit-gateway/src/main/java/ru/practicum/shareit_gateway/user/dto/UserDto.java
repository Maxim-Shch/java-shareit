package ru.practicum.shareit_gateway.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserDto {

    private Long id; //— уникальный идентификатор пользователя;
    private String name; //имя или логин пользователя;
    @Email
    @NotNull
    @NotBlank
    private String email; //email, что два пользователя не могут иметь одинаковый адрес электронной почты);

    public UserDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
