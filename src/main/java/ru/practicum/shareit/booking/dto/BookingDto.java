package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class BookingDto {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;

    public BookingDto() {
        super();
    }
}
