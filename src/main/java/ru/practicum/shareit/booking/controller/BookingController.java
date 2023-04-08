package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;
    private final String userHeaderId = "X-Sharer-User-Id";

    @PostMapping
    public Booking add(@Valid @RequestBody BookingDto booking, @RequestHeader(userHeaderId) Long userId) {
        return this.bookingService.add(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public Booking requestBooking(@PathVariable Long bookingId,
                                  @RequestHeader(userHeaderId) Long userId,
                                  @RequestParam Boolean approved) {
        return this.bookingService.requestBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking getBooking(@PathVariable Long bookingId,
                              @RequestHeader(userHeaderId) Long userId) {
        return this.bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<Booking> getAllBookings(@RequestHeader(userHeaderId) Long userId,
                                        @RequestParam(required = false, defaultValue = "ALL") String state) {
        return this.bookingService.getAllBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<Booking> getAllBookingsOwner(@RequestHeader(userHeaderId) Long userId,
                                                @RequestParam(required = false, defaultValue = "ALL") String state) {
        return this.bookingService.getAllBookingsOwner(userId, state);
    }
}
