package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;

/**
 * TODO Sprint add-controllers.
 */

@AllArgsConstructor
@Builder
@Data
public class User {
    int id;
    String name;
    @Email
    String email;
}
