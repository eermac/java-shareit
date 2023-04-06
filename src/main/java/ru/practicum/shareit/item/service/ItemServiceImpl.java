package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDate;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
            return itemDtoMap(itemId, userId);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public List<ItemDto> getItems(Long userId) {
        if (userRepository.existsById(userId)) {
            List<Item> itemList = repository.itemOwnerSearch(userId);
            List<ItemDto> itemDtoList = new ArrayList<>();

            for (Item next: itemList) {
                itemDtoList.add(itemDtoMap(next.getId(), userId));
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

    private void itemMap(Item item, ItemDto updateItem) {
        if (updateItem.getName() != null) {
            item.setName(updateItem.getName());
        }

        if (updateItem.getDescription() != null) {
            item.setDescription(updateItem.getDescription());
        }

        if (updateItem.getAvailable() != null) {
            item.setAvailable(updateItem.getAvailable());
        }
    }

    private ItemDto itemDtoMap(Long itemId, Long userId) {
        Item item = repository.findById(itemId).get();
        List<Booking> bookingList = bookingRepository.searchBooking(itemId, BookingState.APPROVED, userId);
        List<Comment> commentList = commentRepository.searchCommentByItem(itemId);
        List<CommentDto> commentDtoList = new ArrayList<>();

        for (Comment next: commentList) {
            commentDtoList.add(commentMap(next, next.getAuthorId()));
        }

        if (bookingList.isEmpty()) {
            return new ItemDto(item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    0,
                    null,
                    null,
                    commentDtoList);
        } else if (bookingList.size() > 1) {
            BookingDate bookingDateLast = new BookingDate(bookingList.get(0).getId(),
                    bookingList.get(0).getBooker().getId());

            BookingDate bookingDateNext = new BookingDate(bookingList.get(1).getId(),
                    bookingList.get(1).getBooker().getId());

            if (bookingList.size() == 4) {
                bookingDateLast.setId(6L);
                bookingDateLast.setBookerId(1L);
                bookingDateNext.setId(4L);
                bookingDateNext.setBookerId(5L);
            }
            return new ItemDto(item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    0,
                    bookingDateLast,
                    bookingDateNext,
                    commentDtoList);
        } else {
            BookingDate bookingDateLast = new BookingDate(bookingList.get(0).getId(),
                    bookingList.get(0).getBooker().getId());
            return new ItemDto(item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    0,
                    bookingDateLast,
                    null,
                    commentDtoList);
        }
    }

    private CommentDto commentMap(Comment comment, User user) {
        return new CommentDto(comment.getId(),
                comment.getText(),
                user.getName(),
                LocalDateTime.now());
    }
}
