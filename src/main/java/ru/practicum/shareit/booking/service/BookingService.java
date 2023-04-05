package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    Booking add(BookingDto booking, Long userId);

    Booking requestBooking(Long bookingId, Long userId, Boolean approved);

    Booking getBooking(Long bookingId, Long userId);

    List<Booking> getAllBookings(Long userId, String state);

    List<Booking> getAllBookingsOwner(Long userId, String state);
}
