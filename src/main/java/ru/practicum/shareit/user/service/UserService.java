package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import javax.xml.bind.ValidationException;
import java.util.Collection;

public interface UserService {
    UserDto createUser(UserDto userDto) throws ValidationException;

    UserDto getUserById(Long userId);

    UserDto updateUser(Long userId, UserDto userDto) throws ValidationException;

    void deleteUser(Long userId);

    Collection<UserDto> findAll();
}
