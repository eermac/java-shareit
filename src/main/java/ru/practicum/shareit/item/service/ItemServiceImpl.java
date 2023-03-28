package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        if(userRepository.existsById(userId) & repository.existsById(itemId)){
            Item newItem = repository.findById(itemId).orElseThrow();
            itemMap(newItem, item);
            return this.repository.save(newItem);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public Item getItem(Long itemId, Long userId) {
        if(repository.existsById(itemId) & userRepository.existsById(userId)) {
            return repository.findById(itemId).orElseThrow();
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public List<Item> getItems(Long userId) {
        if(userRepository.existsById(userId)) {
            return repository.findByOwner(userId);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public List<Item> getItemsForSearch(String text) {
        if(text.isEmpty()){
            return new ArrayList<>();
        } else return repository.itemSearch(text);
    }

    private void itemMap(Item item, ItemDto updateItem) {
        if(updateItem.getName() != null) {
            item.setName(updateItem.getName());
        }

        if(updateItem.getDescription() != null) {
            item.setDescription(updateItem.getDescription());
        }

        if(updateItem.getAvailable() != null) {
            item.setAvailable(updateItem.getAvailable());
        }
    }
}
