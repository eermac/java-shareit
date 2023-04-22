package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    private void add() {
        Item item = new Item(null, "name", "description", true, null, null);
        itemRepository.save(item);
    }

    @Test
    void itemOwnerSearch() {
        Long ownerId = 5L;
        List<Item> itemList = itemRepository.itemOwnerSearch(ownerId);

        assertTrue(itemList.isEmpty());
    }

    @Test
    void itemRequestSearch() {
        Long requestId = 1L;
        List<Item> itemList = itemRepository.itemRequestSearch(requestId);

        assertTrue(itemList.isEmpty());
    }

    @Test
    void itemSearch() {
        String text = "testTesxt";
        List<Item> itemList = itemRepository.itemSearch(text);

        assertTrue(itemList.isEmpty());
    }

    @AfterEach
    private void delete() {
        itemRepository.deleteAll();
    }
}
