package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User add(User user);

    User update(User user);

    User updatePatch(UserDto user, Integer userId);

    User getUser(Integer userId);

    User deleteUser(Integer userId);

    List<User> getAll();
}
