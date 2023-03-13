package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */

@AllArgsConstructor
@Builder
@Data
public class ItemDto {
    private String name;
    private String description;
    private Boolean available;
    private int request;
}
