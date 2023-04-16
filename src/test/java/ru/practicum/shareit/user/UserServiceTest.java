package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    @Test
    void getAll() {
        List<User> testUsers = List.of(new User());
        when(userService.getAll()).thenReturn(testUsers);

        ResponseEntity<List<User>> response = ResponseEntity.ok(userController.getAll());

        assertEquals(testUsers, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getUser() {
        long userId = 0L;
        User testUser = new User();
        when(userService.getUser(userId)).thenReturn(testUser);

        ResponseEntity<User> response = ResponseEntity.ok(userController.getUser(userId));
        User user = userService.getUser(userId);

        assertEquals(testUser, user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void add() {
        User testUser = new User();
        when(userService.add(testUser)).thenReturn(testUser);

        ResponseEntity<User> response = ResponseEntity.ok(userController.add(testUser));
        User user = userService.add(testUser);

        assertEquals(testUser, user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void update() {
        User testUser = new User();
        when(userService.update(testUser)).thenReturn(testUser);

        ResponseEntity<User> response = ResponseEntity.ok(userController.update(testUser));
        User user = userService.update(testUser);

        assertEquals(testUser, user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
