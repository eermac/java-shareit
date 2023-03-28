package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings", schema = "public")
@AllArgsConstructor
@Builder
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;
    @Column(name = "start_date", nullable = false)
    private LocalDateTime end;
    @Column(name = "item_id", nullable = false)
    private Long item;
    @Column(name = "booker_id", nullable = false)
    private Long booker;
    private String status; //сделать его через ключевое слово
}
