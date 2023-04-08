package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        return this.userService.add(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return this.userService.update(user);
    }

    @PatchMapping("/{userId}")
    public User updatePatch(@Valid @RequestBody UserDto user, @PathVariable Long userId) {
        return this.userService.updatePatch(user, userId);
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable Long userId) {
        return this.userService.getUser(userId);
    }

    @GetMapping
    public List<User> getAll() {
        return this.userService.getAll();
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        this.userService.delete(userId);
    }
}
