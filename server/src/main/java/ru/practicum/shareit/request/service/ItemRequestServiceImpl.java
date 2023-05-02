package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequest add(ItemRequest itemRequest, Long userId) {
        if (itemRequest.getDescription() != null) {
            itemRequest.setRequestor(userRepository.findById(userId).get());
            itemRequest.setCreated(LocalDateTime.now());
            return itemRequestRepository.save(itemRequest);
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @Override
    public List<ItemRequestDto> getMyRequests(Long userId) {
        if (userRepository.existsById(userId)) {
            List<ItemRequestDto> requestDtoList = new ArrayList<>();
            for (ItemRequest next: itemRequestRepository.searchRequestByUser(userId)) {
                next.setItems(itemRepository.itemRequestSearch(next.getId()));
                requestDtoList.add(ItemRequestMapper.itemRequestMap(next));
            }
            return requestDtoList;
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public List<ItemRequestDto> getAllItemRequestsOtherUsers(Long userId, int from, int size) {
        if (from >= 0 && size > 0) {
            List<Item> itemList = itemRepository.itemOwnerSearch(userId);
            List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
            for (Item next: itemList) {
                if (next.getRequestId() != null) {
                    PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
                    itemRequestDtoList = itemRequestRepository.findAllByIdOrderByCreated(next.getRequestId(), page)
                            .map(ItemRequestMapper::itemRequestMap)
                            .getContent();
                }
            }

            for (ItemRequestDto next: itemRequestDtoList) {
                next.setItems(itemRepository.itemRequestSearch(next.getId()));
            }

            return itemRequestDtoList;
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ItemRequestDto getRequest(Long userId, Long requestId) {
        if (userRepository.existsById(userId) && itemRequestRepository.existsById(requestId)) {
            ItemRequest itemRequest = itemRequestRepository.findById(requestId).get();
            itemRequest.setItems(itemRepository.itemRequestSearch(requestId));
            return ItemRequestMapper.itemRequestMap(itemRequest);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
