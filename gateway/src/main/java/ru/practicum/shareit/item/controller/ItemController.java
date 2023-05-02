package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoPatch;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;
    private final String userHeaderId = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Valid ItemDto requestDto,
                                      @RequestHeader(userHeaderId) Long userId) {
        return itemClient.add(requestDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestBody @Valid CommentDto requestDto,
                                             @PathVariable Long itemId,
                                      @RequestHeader(userHeaderId) Long userId) {
        return itemClient.addComment(requestDto, userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updatePatch(@RequestBody @Valid ItemDtoPatch requestDto,
                                      @PathVariable Long itemId,
                                      @RequestHeader(userHeaderId) Long userId) {
        return itemClient.updatePatch(requestDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable Long itemId,
                                          @RequestHeader(userHeaderId) Long userId) {
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader(userHeaderId) Long userId) {
        return itemClient.getItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsForSearch(@RequestParam(defaultValue = "", required = false) String text) {
        return itemClient.getItemsForSearch(text);
    }
}
