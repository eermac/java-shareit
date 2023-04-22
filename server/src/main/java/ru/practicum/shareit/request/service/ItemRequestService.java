package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequest add(ItemRequest itemRequest, Long userId);

    List<ItemRequestDto> getMyRequests(Long userId);

    List<ItemRequestDto> getAllItemRequestsOtherUsers(Long userId, int from, int size);

    ItemRequestDto getRequest(Long userId, Long requestId);
}
