package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("Select b from Booking b where b.booker.id = (?1) " +
            "AND (?2 = 'CURRENT' AND CURRENT_TIMESTAMP BETWEEN b.start AND b.end " +
            "OR ?2 = 'PAST' AND b.end < CURRENT_TIMESTAMP " +
            "OR ?2 = 'FUTURE' AND b.start > CURRENT_TIMESTAMP " +
            "OR ?2 = 'WAITING' AND b.status = 'WAITING' " +
            "OR ?2 = 'REJECTED' AND b.status = 'REJECTED' " +
            "OR ?2 = 'ALL') ORDER BY b.end DESC ")
    List<Booking> bookingSearch(Long userId, String state);

    @Query("Select b from Booking b where b.item.owner.id = (?1) " +
            "AND (?2 = 'CURRENT' AND CURRENT_TIMESTAMP BETWEEN b.start AND b.end " +
            "OR ?2 = 'PAST' AND b.end < CURRENT_TIMESTAMP " +
            "OR ?2 = 'FUTURE' AND b.start > CURRENT_TIMESTAMP " +
            "OR ?2 = 'WAITING' AND b.status = 'WAITING' " +
            "OR ?2 = 'REJECTED' AND b.status = 'REJECTED' " +
            "OR ?2 = 'ALL') ORDER BY b.start DESC ")
    List<Booking> bookingSearchOwner(Long userId, String state);

    @Query("Select b from Booking b " +
            "where b.item.id = ?1 " +
            "and b.status = ?2 " +
            "and b.item.owner.id = ?3 " +
            "order by b.start asc")
    List<Booking> searchBooking(Long itemId, BookingState bookingState, Long userId);

    @Query("Select b from Booking b " +
            "where b.item.id = ?1 " +
            "and b.status = ?3 " +
            "and b.booker.id = ?2 " +
            "and b.end < CURRENT_TIMESTAMP")
    List<Booking> searchBookingForComment(Long itemId, Long userId, BookingState bookingState);
}
