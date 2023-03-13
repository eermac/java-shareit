package ru.practicum.shareit.item.model;

import ru.practicum.shareit.request.ItemRequest;
import lombok.*;
/**
 * TODO Sprint add-controllers.
 */

@AllArgsConstructor
@Builder
@Data
public class Item {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private Integer owner;
    private ItemRequest request;
}
