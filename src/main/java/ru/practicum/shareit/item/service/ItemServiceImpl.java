package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;

    @Override
    public Item add(Item item, Long userId) {
        if(userRepository.existsById(userId)){
            item.setOwner(userId);
            return this.repository.save(item);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public Item updatePatch(ItemDto item, Long itemId, Long userId) {
        return null;
    }

    @Override
    public Item getItem(Long itemId) {
        return null;
    }

    @Override
    public List<Item> getItems(Long userId) {
        return null;
    }

    @Override
    public List<Item> getItemsForSearch(String text) {
        return null;
    }
}
