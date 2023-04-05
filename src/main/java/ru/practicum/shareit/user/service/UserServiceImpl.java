package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public User add(User user) {
        return this.repository.save(user);
    }

    @Override
    public User update(User user) {
        return this.repository.save(user);
    }

    @Override
    public User updatePatch(UserDto updateUser, Long userId) {
        User newUser = this.repository.findById(userId).orElseThrow();
        userMap(newUser, updateUser);
        return this.repository.save(newUser);
    }

    @Override
    public List<User> getAll() {
        return this.repository.findAll();
    }

    @Override
    public User getUser(Long id) {
        if (this.repository.existsById(id)) {
            return this.repository.findById(id).orElseThrow();
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public void delete(Long userId) {
        this.repository.deleteById(userId);
    }

    private void userMap(User newUser, UserDto updateUser) {
        if (updateUser.getEmail() != null) {
            newUser.setEmail(updateUser.getEmail());
        }

        if (updateUser.getName() != null) {
            newUser.setName(updateUser.getName());
        }
    }
}
