package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item add(Item item, Long userId);
    Item updatePatch(ItemDto item, Long itemId, Long userId);
    ItemDto getItem(Long itemId, Long userId);
    CommentDto addComment(Comment comment, Long itemId, Long userId);
    List<ItemDto> getItems(Long userId);
    List<Item> getItemsForSearch(String text);
}
