package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
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
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    public Booking add(BookingDto booking, Long userId) {
            if (checkBooking(booking, userId)) {
                return bookingRepository.save(BookingMapper.bookingMap(booking,
                        userId,
                        itemRepository.findById(booking.getItemId()).get(),
                        userRepository.findById(userId).get()));
            } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @Override
    public Booking requestBooking(Long bookingId, Long userId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).get();

        if (booking.getItem().getOwner().getId().equals(userId)) {
            if (!booking.getStatus().equals(BookingState.APPROVED)) {
                if (approved) booking.setStatus(BookingState.APPROVED);
                else booking.setStatus(BookingState.REJECTED);

                return bookingRepository.save(booking);
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
      } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public Booking getBooking(Long bookingId, Long userId) {
        if (userRepository.existsById(userId) && bookingRepository.existsById(bookingId)) {
            Booking booking = bookingRepository.findById(bookingId).get();
            if (booking.getItem().getOwner().getId().equals(userId) || booking.getBooker().getId().equals(userId)) {
                return booking;
            } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public List<Booking> getAllBookings(Long userId, String state) {
        if (userRepository.existsById(userId)) {
            if (checkState(state)) {
                return bookingRepository.bookingSearch(userId, state);
            } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public List<Booking> getAllBookingsOwner(Long userId, String state) {
        if (!itemRepository.itemOwnerSearch(userId).isEmpty()) {
            if (checkState(state)) {
                return bookingRepository.bookingSearchOwner(userId, state);
            } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    private boolean checkBooking(BookingDto booking, Long userId) {
        if (!itemRepository.existsById(booking.getItemId())
                || !userRepository.existsById(userId)
                || itemRepository.findById(booking.getItemId()).get().getOwner().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (booking.getStart() == null
                || booking.getEnd() == null
                || booking.getStart().isEqual(booking.getEnd())
                || booking.getStart().isAfter(booking.getEnd())
                || booking.getStart().isBefore(LocalDateTime.now())
                || !itemRepository.findById(booking.getItemId()).get().getAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return true;
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

