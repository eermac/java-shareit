package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
public class ItemRequest {
    int id;
    String description;
    User requestor;
    LocalDateTime created;
}
