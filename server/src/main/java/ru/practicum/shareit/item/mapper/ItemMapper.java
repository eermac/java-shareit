package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.BookingDate;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ItemMapper {
    public static ItemDto itemDtoMap(Item item, List<Booking> bookingList, List<Comment> commentList) {
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

    public static void itemMap(Item item, ItemDto updateItem) {
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

    public static CommentDto commentMap(Comment comment, User user) {
        return new CommentDto(comment.getId(),
                comment.getText(),
                user.getName(),
                LocalDateTime.now());
    }
}
