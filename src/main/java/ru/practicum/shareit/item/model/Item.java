package ru.practicum.shareit.item.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "items", schema = "public")
@Getter
@Setter
@ToString
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String description;
    @Column(name = "is_available", nullable = false)
    @NotNull
    private Boolean available;
    @Column(name = "owner_id")
    private Long owner;
    @Column(name = "request_id")
    private Long request;
}
