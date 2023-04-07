package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.item.mapper.ItemMapper.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public Item add(Item item, Long userId) {
        if (userRepository.existsById(userId)) {
            item.setOwner(userRepository.findById(userId).get());
            return this.repository.save(item);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public Item updatePatch(ItemDto item, Long itemId, Long userId) {
        if (userRepository.existsById(userId) && repository.existsById(itemId)) {
            Item newItem = repository.findById(itemId).orElseThrow();
            itemMap(newItem, item);
            return this.repository.save(newItem);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public ItemDto getItem(Long itemId, Long userId) {
        if (repository.existsById(itemId) && userRepository.existsById(userId)) {
            List<Booking> bookingList = bookingRepository.searchBooking(itemId, BookingState.APPROVED, userId);
            List<Comment> commentList = commentRepository.searchCommentByItem(itemId);
            return itemDtoMap(repository.findById(itemId).get(), bookingList, commentList);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public List<ItemDto> getItems(Long userId) {
        if (userRepository.existsById(userId)) {
            List<Item> itemList = repository.itemOwnerSearch(userId);
            List<ItemDto> itemDtoList = new ArrayList<>();

            for (Item next: itemList) {
                itemDtoList.add(itemDtoMap(next,
                        bookingRepository.searchBooking(next.getId(), BookingState.APPROVED, userId),
                        commentRepository.searchCommentByItem(next.getId())));
            }

            return itemDtoList;
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public CommentDto addComment(Comment comment, Long itemId, Long userId) {
            List<Booking> booking = bookingRepository.searchBookingForComment(itemId, userId, BookingState.APPROVED);
            if (!comment.getText().isBlank() && !booking.isEmpty()) {
                comment.setAuthorId(booking.get(0).getBooker());
                comment.setItemId(booking.get(0).getItem());
                return commentMap(commentRepository.save(comment), booking.get(0).getBooker());
            } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @Override
    public List<Item> getItemsForSearch(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        } else return repository.itemSearch(text);
    }
}
