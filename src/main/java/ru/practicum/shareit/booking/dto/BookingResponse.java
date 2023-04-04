package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class BookingResponse {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingState status;
    private User booker;
    private Item item;
}
