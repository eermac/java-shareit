package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    private ItemService itemService;
    @InjectMocks
    private ItemController itemController;

    @Test
    void add() {
        Item item = new Item();
        Long userId = 1L;
        when(itemService.add(item, userId)).thenReturn(item);

        ResponseEntity<Item> response = ResponseEntity.ok(itemController.add(item, userId));
        Item testItem = itemService.add(item, userId);

        assertEquals(testItem, item);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getItem() {
        ItemDto item = new ItemDto();
        Long itemId = 1L;
        Long userId = 1L;
        when(itemService.getItem(itemId, userId)).thenReturn(item);

        ResponseEntity<ItemDto> response = ResponseEntity.ok(itemController.getItem(itemId, userId));
        ItemDto testItem = itemService.getItem(itemId, userId);

        assertEquals(testItem, item);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getItems() {
        List<ItemDto> item = List.of(new ItemDto());
        Long userId = 1L;
        when(itemService.getItems(userId)).thenReturn(item);

        ResponseEntity<List<ItemDto>> response = ResponseEntity.ok(itemController.getItems(userId));
        List<ItemDto> testItem = itemService.getItems(userId);

        assertEquals(testItem, item);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getItemsForSearch() {
        List<Item> item = List.of(new Item());
        String text = "text";
        when(itemService.getItemsForSearch(text)).thenReturn(item);

        ResponseEntity<List<Item>> response = ResponseEntity.ok(itemController.getItemsForSearch(text));
        List<Item> testItem = itemService.getItemsForSearch(text);

        assertEquals(testItem, item);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
