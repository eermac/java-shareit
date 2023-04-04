package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByItemOrderByEndDesc(Long itemId);
    List<Booking> findByItemOrderByStartAsc(Long itemId);
    List<Booking> findByItemAndStatusOrderByEndDesc(Long itemId, BookingState status);
    List<Booking> findByBookerOrderByEndDesc(Long bookerId);
    List<Booking> findByBookerAndStatusOrderByEndDesc(Long bookerId, BookingState status);
    Booking findFirstByItemAndBooker(Long itemId, Long booker);
}
