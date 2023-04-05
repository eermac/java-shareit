package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
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
    private final String USER_HEADER_ID = "X-Sharer-User-Id";

    @PostMapping
    public Item add(@Valid @RequestBody Item item, @RequestHeader(USER_HEADER_ID) Long userId) {
        log.info("Добавляем вещь");
        return this.itemService.add(item, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto add(@Valid @RequestBody Comment comment,
                          @RequestHeader(USER_HEADER_ID) Long userId,
                          @PathVariable Long itemId) {
        log.info("Добавляем комментарий");
        return this.itemService.addComment(comment, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public Item updatePatch(@RequestBody ItemDto item,
                            @PathVariable Long itemId,
                            @RequestHeader(USER_HEADER_ID) Long userId) {
        log.info("Обновляем данные товара");
        return this.itemService.updatePatch(item, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId, @RequestHeader(USER_HEADER_ID) Long userId) {
        return this.itemService.getItem(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader(USER_HEADER_ID) Long userId) {
        return this.itemService.getItems(userId);
    }

    @GetMapping("/search")
    public List<Item> getItemsForSearch(@RequestParam(defaultValue = "", required = false) String text) {
        return this.itemService.getItemsForSearch(text);
    }
}
