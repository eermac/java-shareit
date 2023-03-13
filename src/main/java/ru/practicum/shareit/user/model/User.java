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
    private int id;
    private String name;
    @Email
    private String email;
}
