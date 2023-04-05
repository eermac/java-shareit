package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User add(User user);
    User update(User user);
    User updatePatch(UserDto updateUser, Long userId);
    List<User> getAll();
    void delete(Long id);
    User getUser(Long id);
}
