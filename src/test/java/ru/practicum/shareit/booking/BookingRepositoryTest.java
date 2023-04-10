package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    private void add() {
        Booking booking = new Booking(null, LocalDateTime.now(), LocalDateTime.now(), null, null, BookingState.WAITING);
        bookingRepository.save(booking);
    }

    @Test
    void bookingSearch() {
        List<Booking> bookingList = bookingRepository.bookingSearch(1L, "WAITING");

        assertTrue(bookingList.isEmpty());
    }

    @Test
    void bookingSearchOwner() {
        List<Booking> bookingList = bookingRepository.bookingSearchOwner(1L, "WAITING");

        assertTrue(bookingList.isEmpty());
    }

    @Test
    void searchBooking() {
        List<Booking> bookingList = bookingRepository.searchBooking(1L, BookingState.WAITING, 1L);

        assertTrue(bookingList.isEmpty());
    }

    @Test
    void searchBookingForComment() {
        List<Booking> bookingList = bookingRepository.searchBookingForComment(1L, 1L, BookingState.WAITING);

        assertTrue(bookingList.isEmpty());
    }

    @AfterEach
    private void delete() {
        bookingRepository.deleteAll();
    }
}
