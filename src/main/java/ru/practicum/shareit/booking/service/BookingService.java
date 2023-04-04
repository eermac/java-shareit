package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    BookingResponse add(BookingDto booking, Long userId);
    BookingResponse requestBooking(Long bookingId, Long userId, Boolean approved);
    BookingResponse getBooking(Long bookingId, Long userId);
    List<BookingResponse> getAllBookings(Long userId, String state);
    List<BookingResponse> getAllBookingsOwner(Long userId, String state);
}
