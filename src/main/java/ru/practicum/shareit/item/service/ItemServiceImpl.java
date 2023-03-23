package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage) {
        this.itemStorage = itemStorage;
    }

    @Override
    public Item add(ItemDto item, Integer userId) {
        return this.itemStorage.add(item, userId);
    }

    @Override
    public Item updatePatch(ItemDto item, Integer itemId, Integer userId) {
        return this.itemStorage.updatePatch(item, itemId, userId);
    }

    @Override
    public Item getItem(Integer itemId) {
        return this.itemStorage.getItem(itemId);
    }

    @Override
    public List<Item> getItems(Integer userId) {
        return this.itemStorage.getItems(userId);
    }

    @Override
    public List<Item> getItemsForSearch(String text) {
        return this.itemStorage.getItemsForSearch(text);
    }
}
