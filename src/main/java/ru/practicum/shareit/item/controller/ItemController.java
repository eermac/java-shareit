package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private final String userHeaderId = "X-Sharer-User-Id";

    @PostMapping
    public Item add(@Valid @RequestBody Item item, @RequestHeader(userHeaderId) Long userId) {
        log.info("Добавляем вещь");
        return this.itemService.add(item, userId);
    }

    @PatchMapping("/{itemId}")
    public Item updatePatch(@RequestBody ItemDto item, @PathVariable Long itemId, @RequestHeader(userHeaderId) Long userId) {
        log.info("Обновляем данные товара");
        return this.itemService.updatePatch(item, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public Item getItem(@PathVariable Long itemId) {
        return this.itemService.getItem(itemId);
    }

    @GetMapping
    public List<Item> getItems(@RequestHeader(userHeaderId) Long userId) {
        return this.itemService.getItems(userId);
    }

    @GetMapping("/search")
    public List<Item> getItemsForSearch(@RequestParam(defaultValue = "", required = false) String text) {
        return this.itemService.getItemsForSearch(text);
    }

}
