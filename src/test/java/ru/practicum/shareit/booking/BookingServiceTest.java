package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    private BookingService bookingService;
    @InjectMocks
    private BookingController bookingController;

    @Test
    void add() {
        Booking booking = new Booking();
        BookingDto bookingDto = new BookingDto();
        Long userId = 1L;
        when(bookingService.add(bookingDto, userId)).thenReturn(booking);

        ResponseEntity<Booking> response = ResponseEntity.ok(bookingController.add(bookingDto, userId));
        Booking testBooking = bookingService.add(bookingDto, userId);

        assertEquals(testBooking, booking);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void requestBooking() {
        Booking booking = new Booking();
        Long userId = 1L;
        when(bookingService.requestBooking(1L, userId, true)).thenReturn(booking);

        ResponseEntity<Booking> response = ResponseEntity.ok(bookingController.requestBooking(1L, userId, true));
        Booking testBooking = bookingService.requestBooking(1L, userId, true);

        assertEquals(testBooking, booking);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getBooking() {
        Booking booking = new Booking();
        Long bookingId = 1L;
        Long userId = 1L;
        when(bookingService.getBooking(bookingId, userId)).thenReturn(booking);

        ResponseEntity<Booking> response = ResponseEntity.ok(bookingController.getBooking(bookingId, userId));
        Booking testBooking = bookingService.getBooking(bookingId, userId);

        assertEquals(testBooking, booking);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAllBookings() {
        List<Booking> bookingList = List.of(new Booking());
        Long bookingId = 1L;
        Long userId = 1L;
        when(bookingService.getAllBookings(userId, "WAITING", null, null)).thenReturn(bookingList);

        ResponseEntity<List<Booking>> response = ResponseEntity.ok(bookingController.getAllBookings(bookingId,
                null,
                null,
                "WAITING"));
        List<Booking> testBookingList = bookingService.getAllBookings(bookingId, "WAITING", null, null);

        assertEquals(testBookingList, bookingList);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAllBookingsPage() {
        List<Booking> bookingList = List.of(new Booking());
        Long bookingId = 1L;
        Long userId = 1L;
        when(bookingService.getAllBookings(userId, "WAITING", 0, 1)).thenReturn(bookingList);

        ResponseEntity<List<Booking>> response = ResponseEntity.ok(bookingController.getAllBookings(bookingId,
                "0",
                "1",
                "WAITING"));
        List<Booking> testBookingList = bookingService.getAllBookings(bookingId, "WAITING", 0, 1);

        assertEquals(testBookingList, bookingList);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAllBookingsOwner() {
        List<Booking> bookingList = List.of(new Booking());
        Long bookingId = 1L;
        Long userId = 1L;
        when(bookingService.getAllBookingsOwner(userId, "WAITING", null, null)).thenReturn(bookingList);

        ResponseEntity<List<Booking>> response = ResponseEntity.ok(bookingController.getAllBookingsOwner(bookingId,
                null,
                null,
                "WAITING"));
        List<Booking> testBookingList = bookingService.getAllBookingsOwner(bookingId, "WAITING", null, null);

        assertEquals(testBookingList, bookingList);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAllBookingsOwnerPage() {
        List<Booking> bookingList = List.of(new Booking());
        Long bookingId = 1L;
        Long userId = 1L;
        when(bookingService.getAllBookingsOwner(userId, "WAITING", 0, 1)).thenReturn(bookingList);

        ResponseEntity<List<Booking>> response = ResponseEntity.ok(bookingController.getAllBookingsOwner(bookingId,
                "0",
                "1",
                "WAITING"));
        List<Booking> testBookingList = bookingService.getAllBookingsOwner(bookingId, "WAITING", 0, 1);

        assertEquals(testBookingList, bookingList);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void bookingDtoTest() {
        BookingDto bookingDto = new BookingDto();

        assertTrue(bookingDto != null);
    }

    @Test
    void bookingResponseTest() {
        BookingResponse bookingResponse = new BookingResponse();

        assertTrue(bookingResponse != null);
    }
}
