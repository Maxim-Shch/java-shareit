package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private final UserDto userDto = UserDto.builder().id(1L).name("UserName").email("user@email.ru").build();
    private final UserUpdateDto userUpdateDto = UserUpdateDto.builder().name("UserUpdName").email("user@email.ru").build();

    @Test
    void testCreateUser() {
        when(userRepository.save(any()))
                .thenReturn(UserMapper.toUser(userDto));
        UserDto savedUser = userService.createUser(userDto);

        assertEquals(savedUser, userDto);
        verify(userRepository).save(any());
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(UserMapper.toUser(userDto)));
        UserDto dto = userService.getUserById(anyLong());

        assertEquals(userDto, dto);
    }

    @Test
    void testGetByIdWithIncorrectParameter() {
        when(userRepository.findById(100L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> userService.getUserById(100L));
        assertEquals("Пользователь с id = 100 не найден.", exception.getMessage());

        verify(userRepository).findById(100L);
    }

    @Test
    void testFindAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(UserMapper.toUser(userDto)));

        Collection<UserDto> allDtoUsers = userService.findAll();

        assertFalse(allDtoUsers.isEmpty());
        assertEquals(1, allDtoUsers.size());
    }

    @Test
    void testFindAllUsersWithEmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        Collection<UserDto> allDtoUsers = userService.findAll();

        assertTrue(allDtoUsers.isEmpty());
    }

    @Test
    void testUpdateUser() {
        userDto.setName("UserUpdName");
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(UserMapper.toUser(userDto)));

        UserDto updatedUserDto = userService.updateUser(userDto.getId(), userUpdateDto);

        assertEquals("UserUpdName", updatedUserDto.getName());
        assertEquals(userDto.getName(), updatedUserDto.getName());
        assertEquals(userDto.getEmail(), updatedUserDto.getEmail());
    }

    @Test
    void testUpdatedNotFoundUser() {
        when(userRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            userService.updateUser(100L, userUpdateDto);
        }, "NotFoundException при обновлении несуществующего пользователя");

        verify(userRepository).findById(100L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);

        assertThrows(NotFoundException.class, () -> userService.getUserById(1L));
    }
}