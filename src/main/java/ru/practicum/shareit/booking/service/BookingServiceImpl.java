package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.StateStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.util.IncorrectParameterException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    public BookingResponse add(BookingDto booking, Long userId) {
        if (!itemRepository.existsById(booking.getItemId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (itemRepository.findById(booking.getItemId()).get().getOwner().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (userRepository.existsById(userId)) {
            if (checkBooking(booking) && itemRepository.findById(booking.getItemId()).get().getAvailable()) {
                Booking newBooking = bookingRepository.save(bookingMap(booking, userId));
                return bookingMapResponse(newBooking, userId, booking.getItemId());
            } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public BookingResponse requestBooking(Long bookingId, Long userId, Boolean approved) {
        Long itemId = bookingRepository.findById(bookingId).get().getItem();
        Item item = itemRepository.findById(itemId).get();
        User owner = userRepository.findById(item.getOwner()).get();
        if (owner.getId().equals(userId)) {
            if (!bookingRepository.findById(bookingId).get().getStatus().equals(BookingState.APPROVED)) {
                Booking updateBooking = bookingRepository.findById(bookingId).orElseThrow();

                if (approved) updateBooking.setStatus(BookingState.APPROVED);
                else updateBooking.setStatus(BookingState.REJECTED);

                return bookingMapResponse(bookingRepository.save(updateBooking),
                        updateBooking.getBooker(),
                        updateBooking.getItem());
            } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public BookingResponse getBooking(Long bookingId, Long userId) {
        if (userRepository.existsById(userId) && bookingRepository.existsById(bookingId)) {
            if (itemRepository.findById(bookingRepository.findById(bookingId).get().getItem())
                    .get()
                    .getOwner()
                    .equals(userId)
                    || bookingRepository.findById(bookingId).get().getBooker().equals(userId)) {
                Booking booking = bookingRepository.findById(bookingId).orElseThrow();
                return bookingMapResponse(bookingRepository.save(booking), booking.getBooker(), booking.getItem());
            } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public List<BookingResponse> getAllBookings(Long userId, String state) {
        if (userRepository.existsById(userId)) {
            if (checkState(state)) {
                List<Booking> newBookingList = new ArrayList<>();

                if (state.equalsIgnoreCase(BookingState.WAITING.name())) {
                    newBookingList = bookingRepository.findByBookerAndStatusOrderByEndDesc(userId, BookingState.WAITING);
                } else if (state.equalsIgnoreCase(BookingState.REJECTED.name())) {
                    newBookingList = bookingRepository.findByBookerAndStatusOrderByEndDesc(userId, BookingState.REJECTED);
                } else if (state.equalsIgnoreCase(StateStatus.ALL.name())
                        || state.equalsIgnoreCase(StateStatus.FUTURE.name())) {
                    newBookingList = bookingRepository.findByBookerOrderByEndDesc(userId);
                }

                List<BookingResponse> newResponse = new ArrayList<>();

                for (Booking next: newBookingList) {
                    newResponse.add(bookingMapResponse(next, next.getBooker(), next.getItem()));
                }

                return newResponse;
            } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public List<BookingResponse> getAllBookingsOwner(Long userId, String state) {
        if (!itemRepository.findByOwner(userId).isEmpty()) {
            if (checkState(state)) {
                List<Booking> bookingList = new ArrayList<>();
                List<BookingResponse> newResponse = new ArrayList<>();
                List<Item> itemList = itemRepository.findByOwner(userId);

                for (Item next: itemList) {
                    List<Booking> newBookingList;
                    if (state.equalsIgnoreCase(StateStatus.ALL.name())
                            || state.equalsIgnoreCase(StateStatus.FUTURE.name())
                            || state.equalsIgnoreCase(StateStatus.CURRENT.name())
                            || state.equalsIgnoreCase(StateStatus.PAST.name())) {
                        newBookingList = bookingRepository.findByItemOrderByEndDesc(next.getId());
                    } else {
                        newBookingList = bookingRepository.findByItemAndStatusOrderByEndDesc(next.getId(),
                                BookingState.valueOf(state));

                    }
                    bookingList.addAll(newBookingList);
                }

                for (Booking next: bookingList) {
                    newResponse.add(bookingMapResponse(next, next.getBooker(), next.getItem()));
                }

                return newResponse;
            } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    private boolean checkBooking(BookingDto booking) {
        if (booking.getStart() == null || booking.getEnd() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (booking.getStart().isEqual(booking.getEnd())
                || booking.getStart().isAfter(booking.getEnd())
                || booking.getStart().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return true;
    }

    private Booking bookingMap(BookingDto booking, Long userId) {
        return new Booking(null,
                booking.getStart(),
                booking.getEnd(),
                booking.getItemId(),
                userId,
                BookingState.WAITING);
    }

    private BookingResponse bookingMapResponse(Booking booking, Long userId, Long itemId) {
        return new BookingResponse(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                userRepository.findById(userId).get(),
                itemRepository.findById(itemId).get());
    }

    private boolean checkState(String state) {
        if (state.equalsIgnoreCase(StateStatus.ALL.name())
                || state.equalsIgnoreCase(StateStatus.FUTURE.name())
                || state.equalsIgnoreCase(BookingState.WAITING.name())
                || state.equalsIgnoreCase(BookingState.REJECTED.name())
                || state.equalsIgnoreCase(StateStatus.CURRENT.name())
                || state.equalsIgnoreCase(StateStatus.PAST.name())) {
            return true;
        } else throw new IncorrectParameterException(state);
    }
}

