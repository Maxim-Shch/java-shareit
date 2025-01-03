package ru.practicum.shareit_gateway.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit_gateway.user.dto.UserDto;
import ru.practicum.shareit_gateway.user.dto.UserUpdateDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Получен список всех пользователей.");
        return userClient.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<Object> createNewUser(@RequestBody @Valid UserDto userDto) {
        log.info("Создан новый пользователь с именем {}", userDto.getName());
        return userClient.addUser(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id,
                                             @RequestBody @Valid UserUpdateDto userDto) {
        log.info("Пользователь с id {} обновлен", id);
        return userClient.updateUser(id, userDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        log.info("Получен пользователь с id {}", id);
        return userClient.getUserById(id);
    }

    @DeleteMapping(("/{id}"))
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        log.info("Пользователь с id {} удален", id);
        return userClient.deleteUser(id);
    }
}
