package ru.practicum.shareit.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.xml.bind.ValidationException;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Создаём пользователя
    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) throws ValidationException {
        log.info("Создаем пользователя: {}", userDto);
        return userService.createUser(userDto);
    }

    //Получаем пользователя по id
    @GetMapping("/{userId}")
    public UserDto getUserId(@PathVariable("userId") Long userId) {
        log.info("Получаем пользователя по ID: {}", userId);
        return userService.getUserById(userId);
    }

    @GetMapping
    public Collection<UserDto> getAllUsers() {
        return userService.findAll();
    }

    //Обновляем данные пользователя
    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable("userId") Long userId,
                              @RequestBody @Valid UserUpdateDto userDto) throws ValidationException {
        log.info("Обновляем данные пользователя по ID: {}", userId);
        return userService.updateUser(userId, userDto);
    }

    //Удаляем пользователя
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        log.info("Удаляем пользователя по ID: {}", userId);
        userService.deleteUser(userId);
    }
}
