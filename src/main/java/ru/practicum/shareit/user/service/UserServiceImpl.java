package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User add(User user) {
        return userStorage.add(user);
    }

    @Override
    public User update(User user) {
        return userStorage.update(user);
    }

    @Override
    public User updatePatch(UserDto user, Integer userId) {
        return userStorage.updatePatch(user, userId);
    }

    @Override
    public User getUser(Integer userId) {
        return userStorage.getUser(userId);
    }

    @Override
    public User deleteUser(Integer userId) {
        return userStorage.deleteUser(userId);
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }
}
