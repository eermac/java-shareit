package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static Booking bookingMap(BookingDto booking, Long userId, Item item, User user) {
        return new Booking(null,
                booking.getStart(),
                booking.getEnd(),
                item,
                user,
                BookingState.WAITING);
    }
}
