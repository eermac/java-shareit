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
    int id;
    String name;
    String description;
    Boolean available;
    Integer owner;
    ItemRequest request;
}
