package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.controller.UserController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ItemStorage {
    private final UserController userController;
    private Map<Integer, Item> items = new HashMap<>();
    private int idGenerate = 0;

    @Autowired
    public ItemStorage(UserController userController) {
        this.userController = userController;
    }

    public Integer setId() {
        idGenerate++;
        return this.idGenerate;
    }

    public Item add(ItemDto item, Integer userId) {
        log.info("Добавляем вещь");

        if (userController.getUser(userId) != null) {
            if (item.getName() == null | item.getName().isBlank() |
                    item.getDescription() == null | item.getAvailable() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else {
                Item newItem = new Item(setId(),
                                item.getName(),
                                item.getDescription(),
                                item.getAvailable(),
                                userId,
                                null);
                items.put(newItem.getId(), newItem);

                return newItem;
            }
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public Item updatePatch(ItemDto item, Integer itemId, Integer userId) {
        log.info("Обновляем данные товара");

        if (items.containsKey(itemId)) {
            Item updateItem =  transformPatch(item, itemId, userId);
            items.put(itemId, updateItem);
            return updateItem;
        } else throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public Item getItem(Integer itemId) {
        return items.get(itemId);
    }

    public List<Item> getItems(Integer userId) {
        List<Item> itemList = new ArrayList<>();
        for (Item next: items.values()) {
            if (next.getOwner().equals(userId)) {
                itemList.add(next);
            }
        }
        return itemList;
    }

    public List<Item> getItemsForSearch(String text)  {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }

        List<Item> itemList = new ArrayList<>();
        for (Item next: items.values()) {
            if ((next.getName().toLowerCase().contains(text.toLowerCase())
                    | next.getDescription().toLowerCase().contains(text.toLowerCase()))
                    & next.getAvailable() == true) {
                itemList.add(next);
            }
        }
        return itemList;
    }

    public Item transformPatch(ItemDto item, Integer itemId, Integer userId) {
        if (item.getName() == null & item.getDescription() == null & item.getAvailable() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else if (!items.get(itemId).getOwner().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (item.getName() == null) {
            item.setName(items.get(itemId).getName());
        }

        if (item.getDescription() == null) {
            item.setDescription(items.get(itemId).getDescription());
        }

        if (item.getAvailable() == null) {
            item.setAvailable(items.get(itemId).getAvailable());
        }

        return new Item(itemId, item.getName(), item.getDescription(), item.getAvailable(), userId, null);
    }
}
