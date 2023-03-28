package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item add(Item item, Long userId);

    Item updatePatch(ItemDto item, Long itemId, Long userId);

    Item getItem(Long itemId, Long userId);

    List<Item> getItems(Long userId);

    List<Item> getItemsForSearch(String text);
}
