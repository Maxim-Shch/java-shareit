package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import javax.xml.bind.ValidationException;
import java.util.Collection;

public interface UserStorage {

    Collection<User> findAll();

    User createUser(User user) throws ValidationException;

    User updateUser(Long userId, User user) throws ValidationException;

    User findById(Long id);

    void deleteUser(Long userId);
}
