package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BookingDate {
    private Long id;
    private Long bookerId;
    public BookingDate() {
        super();
    }
}
