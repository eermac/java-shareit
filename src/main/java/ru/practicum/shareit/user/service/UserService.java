package ru.practicum.shareit.user.service;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

public interface UserService {
    User add(User user);

    User update(User user);

    User updatePatch(UserDto user, Integer userId);

    User getUser(Integer userId);

    User deleteUser(Integer userId);

    List<User> getAll();
}
