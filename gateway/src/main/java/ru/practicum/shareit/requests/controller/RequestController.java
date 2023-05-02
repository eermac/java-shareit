package ru.practicum.shareit.requests.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.client.RequestClient;
import ru.practicum.shareit.requests.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;
    private final String userHeaderId = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Valid RequestDto requestDto,
                                      @RequestHeader(userHeaderId) Long userId) {
        return requestClient.add(requestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getMyRequests(@RequestHeader(userHeaderId) Long userId) {
        return requestClient.getMyRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequestsOtherUsers(@RequestHeader(userHeaderId) Long userId,
                                          @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                          @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return requestClient.getAllItemRequestsOtherUsers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItem(@PathVariable Long requestId,
                                          @RequestHeader(userHeaderId) Long userId) {
        return requestClient.getRequest(userId, requestId);
    }
}
