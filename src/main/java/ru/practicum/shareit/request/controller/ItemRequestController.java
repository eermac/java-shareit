package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private final String userHeaderId = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequest add(@Valid @RequestBody ItemRequest itemRequest, @RequestHeader(userHeaderId) Long userId) {
        return this.itemRequestService.add(itemRequest, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getMyRequests(@RequestHeader(userHeaderId) Long userId) {
        return this.itemRequestService.getMyRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequestsOtherUsers(@RequestHeader(userHeaderId) Long userId,
                                        @RequestParam(required = false, defaultValue = "0") String from,
                                        @RequestParam(required = false, defaultValue = "1") String size) {
        return this.itemRequestService.getAllItemRequestsOtherUsers(userId,
                Integer.parseInt(from),
                Integer.parseInt(size));
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequest(@RequestHeader(userHeaderId) Long userId, @PathVariable Long requestId) {
        return this.itemRequestService.getRequest(userId, requestId);
    }
}
