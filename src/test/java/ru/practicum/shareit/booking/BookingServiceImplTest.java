package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.util.IncorrectParameterException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
public class BookingServiceImplTest {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    private Booking booking;
    private User user;
    private Item item;

    @BeforeEach
    private void bookingCreate() {
        user = userRepository.save(new User(null,
                "name",
                "email@ya.ru"));
        User userOwner = userRepository.save(new User(null,
                "nam3e",
                "em3ail@ya.ru"));
        item = itemRepository.save(new Item(null,
                "itemName",
                "description",
                true,
                userOwner,
                null));
        booking = bookingRepository.save(new Booking(null,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                item, user, BookingState.WAITING));
    }

    @Test
    void add() {
        BookingDto bookingDto = new BookingDto(booking.getItem().getId(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3));

        Booking newBooking = bookingService.add(bookingDto, user.getId());

        assertEquals(newBooking.getBooker().getEmail(), booking.getBooker().getEmail());
        assertEquals(newBooking.getStatus(), booking.getStatus());
    }

    @Test
    void requestBookingApprove() {
        Booking requestBooking = bookingService.requestBooking(booking.getId(), item.getOwner().getId(), true);

        assertEquals(requestBooking.getBooker().getEmail(), booking.getBooker().getEmail());
        assertEquals(requestBooking.getStatus(), BookingState.APPROVED);
    }

    @Test
    void requestBookingWrongUser() {
        try {
        Booking requestBooking = bookingService.requestBooking(booking.getId(), 100L, true);
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        }
    }

    @Test
    void requestBookingWrongState() {
        booking.setStatus(BookingState.APPROVED);
        try {
            Booking requestBooking = bookingService.requestBooking(booking.getId(), item.getOwner().getId(), true);
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        }
    }

    @Test
    void requestBookingRejected() {
        Booking requestBooking = bookingService.requestBooking(booking.getId(), item.getOwner().getId(), false);

        assertEquals(requestBooking.getBooker().getEmail(), booking.getBooker().getEmail());
        assertEquals(requestBooking.getStatus(), BookingState.REJECTED);
    }

    @Test
    void getBooking() {
        Booking requestBooking = bookingService.getBooking(booking.getId(), item.getOwner().getId());

        assertEquals(requestBooking.getId(), booking.getId());
        assertEquals(requestBooking.getBooker().getEmail(), booking.getBooker().getEmail());
        assertEquals(requestBooking.getStatus(), BookingState.WAITING);
    }

    @Test
    void getBookingWrong() {
        try {
            Booking requestBooking = bookingService.getBooking(booking.getId(), 100L);
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        }
    }

    @Test
    void getBookingWrongOwner() {
        try {
            Booking requestBooking = bookingService.getBooking(booking.getId(), item.getOwner().getId());
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        }
    }

    @Test
    void getAllBookings() {
        List<Booking> requestBooking = bookingService.getAllBookings(booking.getBooker().getId(),
                "WAITING",
                null,
                null);

        assertTrue(requestBooking.size() > 0);
        assertEquals(requestBooking.get(0).getId(), booking.getId());
        assertEquals(requestBooking.get(0).getStatus(), BookingState.WAITING);
    }

    @Test
    void getAllBookingsWithPage() {
        List<Booking> requestBooking = bookingService.getAllBookings(booking.getBooker().getId(),
                "WAITING",
                0,
                1);

        assertTrue(requestBooking.size() > 0);
        assertEquals(requestBooking.get(0).getId(), booking.getId());
        assertEquals(requestBooking.get(0).getStatus(), BookingState.WAITING);
    }

    @Test
    void getAllBookingsWrongUser() {
        try {
            List<Booking> requestBooking = bookingService.getAllBookings(100L,
                    "WAITING",
                    null,
                    null);
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        }
    }

    @Test
    void getAllBookingsWrongState() {
        try {
            List<Booking> requestBooking = bookingService.getAllBookings(booking.getBooker().getId(),
                    "sdfg",
                    null,
                    null);
        } catch (IncorrectParameterException ex) {
            assertEquals("sdfg", ex.getParameter());
        }
    }

    @Test
    void getAllBookingsWrongFrom() {
        try {
            List<Booking> requestBooking = bookingService.getAllBookings(booking.getBooker().getId(),
                    "WAITING",
                    -1,
                    1);
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        }
    }

    @Test
    void getAllBookingsOwner() {
        List<Booking> requestBooking = bookingService.getAllBookingsOwner(item.getOwner().getId(),
                "WAITING",
                null,
                null);

        assertTrue(requestBooking.size() > 0);
        assertEquals(requestBooking.get(0).getId(), booking.getId());
        assertEquals(requestBooking.get(0).getStatus(), BookingState.WAITING);
    }

    @Test
    void getAllBookingsOwnerWithPage() {
        List<Booking> requestBooking = bookingService.getAllBookingsOwner(item.getOwner().getId(),
                "WAITING",
                0,
                1);

        assertTrue(requestBooking.size() > 0);
        assertEquals(requestBooking.get(0).getId(), booking.getId());
        assertEquals(requestBooking.get(0).getStatus(), BookingState.WAITING);
    }

    @Test
    void addWrongEnd() {
        try {
            BookingDto bookingDto = new BookingDto(booking.getItem().getId(),
                    LocalDateTime.now().plusDays(6),
                    LocalDateTime.now().plusDays(3));

            Booking newBooking = bookingService.add(bookingDto, user.getId());
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        }
    }

    @Test
    void addWrongStart() {
        try {
            BookingDto bookingDto = new BookingDto(booking.getItem().getId(),
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(3));

            Booking newBooking = bookingService.add(bookingDto, user.getId());
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        }
    }

    @Test
    void addWrongDate() {
        try {
            BookingDto bookingDto = new BookingDto(booking.getItem().getId(),
                    LocalDateTime.now(),
                    LocalDateTime.now());

            Booking newBooking = bookingService.add(bookingDto, user.getId());
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        }
    }

    @Test
    void addWrongStartNull() {
        try {
            BookingDto bookingDto = new BookingDto(booking.getItem().getId(),
                    null,
                    LocalDateTime.now().plusDays(3));

            Booking newBooking = bookingService.add(bookingDto, user.getId());
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        }
    }

    @Test
    void addWrongEndNull() {
        try {
            BookingDto bookingDto = new BookingDto(booking.getItem().getId(),
                    LocalDateTime.now().plusDays(1),
                    null);

            Booking newBooking = bookingService.add(bookingDto, user.getId());
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        }
    }

    @Test
    void addWrongUser() {
        try {
            BookingDto bookingDto = new BookingDto(booking.getItem().getId(),
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(3));

            Booking newBooking = bookingService.add(bookingDto, 100L);
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        }
    }

    @Test
    void addWrongItem() {
        try {
            BookingDto bookingDto = new BookingDto(6L,
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(3));

            Booking newBooking = bookingService.add(bookingDto, user.getId());
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        }
    }

    @Test
    void addWrongOwner() {
        try {
            BookingDto bookingDto = new BookingDto(booking.getItem().getId(),
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(3));

            Booking newBooking = bookingService.add(bookingDto, 2L);
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        }
    }

    @Test
    void getWrongSizeFrom() {
        try {
            List<Booking> requestBooking = bookingService.getAllBookingsOwner(item.getOwner().getId(),
                    "WAITING",
                    -100,
                    -100);
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        }
    }

    @Test
    void getWrongState() {
        try {
            List<Booking> requestBooking = bookingService.getAllBookingsOwner(item.getOwner().getId(),
                    "sdfkljxv",
                    0,
                    1);
        } catch (IncorrectParameterException ex) {
            assertEquals("sdfkljxv", ex.getParameter());
        }
    }

    @Test
    void getWrongItem() {
        try {
            List<Booking> requestBooking = bookingService.getAllBookingsOwner(100L,
                    "WAITING",
                    0,
                    1);
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        }
    }



    @AfterEach
    private void deleteAll() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

}
