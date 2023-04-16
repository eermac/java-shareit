package ru.practicum.shareit.request.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ItemRequestMapper {
    public static ItemRequestDto itemRequestMap(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                itemRequest.getItems());
    }

    public static List<ItemRequestDto> itemRequestMap(Iterable<ItemRequest> itemRequests) {
        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        for (ItemRequest next: itemRequests) {
            itemRequestDtoList.add(itemRequestMap(next));
        }
        return itemRequestDtoList;
    }
}
