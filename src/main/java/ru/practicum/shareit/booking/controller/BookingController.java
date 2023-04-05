package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;
    private final String USER_HEADER_ID = "X-Sharer-User-Id";

    @PostMapping
    public BookingResponse add(@Valid @RequestBody BookingDto booking, @RequestHeader(USER_HEADER_ID) Long userId) {
        return this.bookingService.add(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponse requestBooking(@PathVariable Long bookingId,
                                  @RequestHeader(USER_HEADER_ID) Long userId,
                                  @RequestParam Boolean approved) {
        return this.bookingService.requestBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponse getBooking(@PathVariable Long bookingId,
                              @RequestHeader(USER_HEADER_ID) Long userId) {
        return this.bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingResponse> getAllBookings(@RequestHeader(USER_HEADER_ID) Long userId,
                                        @RequestParam(required = false, defaultValue = "ALL") String state) {
        return this.bookingService.getAllBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponse> getAllBookingsOwner(@RequestHeader(USER_HEADER_ID) Long userId,
                                                @RequestParam(required = false, defaultValue = "ALL") String state) {
        return this.bookingService.getAllBookingsOwner(userId, state);
    }
}
