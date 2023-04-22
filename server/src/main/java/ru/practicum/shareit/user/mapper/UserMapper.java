package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getName(),
                user.getEmail());
    }

    public static void userMap(User newUser, UserDto updateUser) {
        if (updateUser.getEmail() != null) {
            newUser.setEmail(updateUser.getEmail());
        }

        if (updateUser.getName() != null) {
            newUser.setName(updateUser.getName());
        }
    }
}
