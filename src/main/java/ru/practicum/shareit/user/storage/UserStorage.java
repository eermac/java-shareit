package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class UserStorage {
    private Map<Integer, User> users = new HashMap<>();
    private int idGenerate = 0;

    public Integer setId() {
        idGenerate++;
        return this.idGenerate;
    }

    public User add(User user) {
        log.info("Добавляем пользователя");

        if (validate(user, HttpMethod.POST)) {
            user.setId(setId());
            users.put(user.getId(), user);
        }

        return user;
    }

    public User update(User user) {
        log.info("Обновляем данные пользователя");

        if (validate(user, HttpMethod.PUT) & users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

        return user;
    }

    public User updatePatch(UserDto user, Integer userId) {
        log.info("Обновляем данные пользователя");

        User updateUser = validatePatch(user, HttpMethod.PATCH, userId);

        if (users.containsKey(userId)) {
            users.put(userId, updateUser);
        } else throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

        return updateUser;
    }

    public User getUser(Integer userId) {
        return users.get(userId);
    }

    public User deleteUser(Integer userId) {
        return users.remove(userId);
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public boolean validate(User user, HttpMethod method) {
        List<String> emails = new ArrayList<>();

        for (User next: users.values()) {
            emails.add(next.getEmail());
        }

        if (user.getName() == null) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы", method);
        } else if (user.getEmail() == null) {
            throw new ValidationException("Email пустой", method, 1);
        } else if (emails.contains(user.getEmail())) {
            throw new ValidationException("Email  уже занят", method, 0);
        }
        return true;
    }

    public User validatePatch(UserDto user, HttpMethod method, int userId) {
        if (user.getName() == null & user.getEmail() == null) {
            throw new ValidationException("Проверьте передаваемые данные", method);
        }

        List<String> emails = new ArrayList<>();

        for (User next: users.values()) {
            emails.add(next.getEmail());
        }

        if (user.getEmail() != null & emails.contains(user.getEmail())) {
            if (emails.contains(users.get(userId).getEmail()) & users.get(userId).getEmail().equals(user.getEmail())) {
                user.setEmail(users.get(userId).getEmail());
            } else {
                throw new ValidationException("Email  уже занят", method, 0);
            }
        }

        if (user.getName() == null) {
            user.setName((users.get(userId).getName()));
        }

        if (user.getEmail() == null) {
            user.setEmail(users.get(userId).getEmail());
        }

        return new User(userId, user.getName(), user.getEmail());
    }
}
