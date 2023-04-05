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
            item.setOwner(userId);
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
            List<Item> itemList = repository.findByOwnerOrderByIdAsc(userId);
            List<ItemDto> itemDtoList = new ArrayList<>();

            for (Item next: itemList) {
                itemDtoList.add(itemDtoMap(next.getId(), userId));
            }

            return itemDtoList;
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public CommentDto addComment(Comment comment, Long itemId, Long userId) {
        if (userRepository.existsById(userId) & repository.existsById(itemId)) {
            Booking booking = bookingRepository.findFirstByItemAndBooker(itemId, userId);
            if (!comment.getText().isBlank()
                    && booking.getStatus().equals(BookingState.APPROVED)
                    && LocalDateTime.now().isAfter(booking.getStart())) {
                comment.setAuthorId(userId);
                comment.setItemId(itemId);
                return commentMap(commentRepository.save(comment), userId);
            } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
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
        List<Booking> bookingList = bookingRepository.findByItemOrderByStartAsc(itemId);
        List<Comment> commentList = commentRepository.findByItemId(itemId);
        List<Booking> updateBookingList = new ArrayList<>();
        List<CommentDto> commentDtoList = new ArrayList<>();

        for (Booking next: bookingList) {
            if (next.getStatus().equals(BookingState.APPROVED)) {
                updateBookingList.add(next);
            }
        }

        for (Comment next: commentList) {
            commentDtoList.add(commentMap(next, next.getAuthorId()));
        }

        if (item.getOwner().equals(userId)) {
            if (updateBookingList.isEmpty()) {
                return new ItemDto(item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.getAvailable(),
                        0,
                        null,
                        null,
                        commentDtoList);
            } else if (updateBookingList.size() > 1) {
                BookingDate bookingLast = new BookingDate(updateBookingList.get(0).getId(),
                        updateBookingList.get(0).getBooker());
                BookingDate bookingNext = new BookingDate(updateBookingList.get(1).getId(),
                        updateBookingList.get(1).getBooker());

                return new ItemDto(item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.getAvailable(),
                        0,
                        bookingLast,
                        bookingNext,
                        commentDtoList);
            } else {
                BookingDate bookingLast = new BookingDate(updateBookingList.get(0).getId(),
                        updateBookingList.get(0).getBooker());
                return new ItemDto(item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.getAvailable(),
                        0,
                        bookingLast,
                        bookingLast,
                        commentDtoList);
            }
        } else {
            return new ItemDto(item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    0,
                    null,
                    null,
                    commentDtoList);
        }
    }

    private CommentDto commentMap(Comment comment, Long userId) {
        return new CommentDto(comment.getId(),
                comment.getText(),
                userRepository.findById(userId).get().getName(),
                LocalDateTime.now());
    }
}
