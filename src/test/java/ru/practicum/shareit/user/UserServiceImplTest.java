package ru.practicum.shareit.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void userCreate() {
       user =  userRepository.save(new User(null, "name", "email@ya.ru"));
    }

    @Test
    void add() {
        User newUser = userService.add(user);

        assertEquals(user.getName(), newUser.getName());
        assertEquals(user.getEmail(), newUser.getEmail());
    }

    @Test
    void update() {
        User userUpdate = new User(1L, "name2", "em1ail@ya.ru");

        User newUserUpdate = userService.update(userUpdate);

        assertEquals(userUpdate.getName(), newUserUpdate.getName());
        assertEquals(userUpdate.getEmail(), newUserUpdate.getEmail());
    }

    @Test
    void updatePatch() {
        UserDto userUpdateTest = new UserDto("name3", "em2ail@ya.ru");

        User newUserUpdate = userService.updatePatch(userUpdateTest, user.getId());

        assertEquals(userUpdateTest.getName(), newUserUpdate.getName());
        assertEquals(userUpdateTest.getEmail(), newUserUpdate.getEmail());
    }

    @Test
    void getAll() {
        userService.add(user);
        List<User> userList = userService.getAll();

        assertTrue(userList.size() > 0);
    }

    @Test
    void getUser() {
        User newUserCreate = userService.add(user);
        User findUser = userService.getUser(newUserCreate.getId());

        assertEquals(findUser.getName(), user.getName());
        assertEquals(findUser.getEmail(), user.getEmail());
    }


    @Test
    void delete() {
        User newUserCreate = userService.add(user);
        Long userId = newUserCreate.getId();
        userService.delete(newUserCreate.getId());

        assertFalse(userRepository.existsById(userId));
    }

    @Test
    void userMapper() {
        UserDto userDto = UserMapper.toUserDto(user);

        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());
    }

    @AfterEach
    public void deleteUser() {
        userRepository.deleteAll();
    }
}
