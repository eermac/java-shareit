package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item add(ItemDto item, Integer userId);
    Item updatePatch(ItemDto item, Integer itemId, Integer userId);
    Item getItem(Integer itemId);
    List<Item> getItems(Integer userId);
    List<Item> getItemsForSearch(String text);
}
