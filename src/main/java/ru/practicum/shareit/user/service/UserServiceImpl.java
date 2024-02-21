package ru.practicum.shareit.user.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.xml.bind.ValidationException;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public UserDto createUser(UserDto userDto) throws ValidationException {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userStorage.createUser(user));
    }

    @Override
    public UserDto getUserId(Long userId) {
        return UserMapper.toUserDto(userStorage.findById(userId));
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) throws ValidationException {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userStorage.updateUser(userId, user));
    }

    @Override
    public void deleteUser(Long userId) {
        userStorage.deleteUser(userId);
    }

    @Override
    public Collection<UserDto> findAll() {
        return userStorage.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
