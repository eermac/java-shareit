package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;
import java.util.Set;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("Select b from Booking b where b.booker.id = (?1) " +
            "AND (?2 = 'CURRENT' AND CURRENT_TIMESTAMP BETWEEN b.start AND b.end " +
            "OR ?2 = 'PAST' AND b.end < CURRENT_TIMESTAMP " +
            "OR ?2 = 'FUTURE' AND b.start > CURRENT_TIMESTAMP " +
            "OR ?2 = 'WAITING' AND b.status > 'WAITING' " +
            "OR ?2 = 'REJECTED' AND b.status > 'REJECTED' " +
            "OR ?2 = 'ALL') ORDER BY b.start DESC ")
    List<Booking> bookingSearch(Long userId, String state);

    @Query("Select b from Booking b where b.item.owner.id = (?1) " +
            "AND (?2 = 'CURRENT' AND CURRENT_TIMESTAMP BETWEEN b.start AND b.end " +
            "OR ?2 = 'PAST' AND b.end < CURRENT_TIMESTAMP " +
            "OR ?2 = 'FUTURE' AND b.start > CURRENT_TIMESTAMP " +
            "OR ?2 = 'WAITING' AND b.status > 'WAITING' " +
            "OR ?2 = 'REJECTED' AND b.status > 'REJECTED' " +
            "OR ?2 = 'ALL') ORDER BY b.start DESC ")
    List<Booking> bookingSearchOwner(Long userId, String state);

    List<Booking> findByBookerOrderByEndDesc(Long bookerId);

    List<Booking> findByBookerAndStatusOrderByEndDesc(Long bookerId, BookingState status);

    Booking findFirstByItemAndBooker(Long itemId, Long booker);
}
