package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;

import javax.xml.bind.ValidationException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private Long generatorId = 0L;

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User createUser(User user) throws ValidationException {
        validateEmail(user.getEmail());
        user.setId(++generatorId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(Long userId, User user) throws ValidationException {
        User updatedUser = findById(userId);

        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            if (!user.getEmail().equals(updatedUser.getEmail())) {
                validateEmail(user.getEmail());
            }
            updatedUser.setEmail(user.getEmail());
        }
        users.put(userId, updatedUser);
        return updatedUser;
    }

    @Override
    public User findById(Long id) {
        User user = users.get(id);
        if (user != null) {
            return user;
        } else {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден.", id));
        }
    }

    @Override
    public void deleteUser(Long userId) {
        User user = users.get(userId);
        if (user != null) {
            users.remove(userId);
        } else {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден.", userId));
        }
    }

    private void validateEmail(String email) throws ValidationException {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                throw new ValidationException("Такой email " + email + " уже занят пользователем id " + user.getId());
            }
        }
    }
}
