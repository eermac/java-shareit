package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDate;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private int request;
    private BookingDate lastBooking;
    private BookingDate nextBooking;
    private List<CommentDto> comments;

    public ItemDto() {
        super();
    }
}
