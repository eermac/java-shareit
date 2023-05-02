package ru.practicum.shareit.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    private void add() {
        User user = new User(null, "name", "email@mail.ru");
        userRepository.save(user);
        ItemRequest item = new ItemRequest(null, "discription", user, LocalDateTime.now(), null);
        itemRequestRepository.save(item);
    }

    @Test
    void searchRequestByUser() {
        Long userId = 1L;
        List<ItemRequest> itemList = itemRequestRepository.searchRequestByUser(userId);

        assertTrue(itemList.isEmpty());
    }

    @Test
    void findAllByIdOrderByCreated() {
        Long userId = 5L;
        Page<ItemRequest> itemList = itemRequestRepository.findAllByIdOrderByCreated(userId, Pageable.unpaged());

        assertTrue(itemList.isEmpty());
    }

    @AfterEach
    private void delete() {
        itemRequestRepository.deleteAll();
    }
}
