package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "bookings", schema = "public")
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;
    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;
    @Column(name = "item_id", nullable = false)
    private Long item;
    @Column(name = "booker_id", nullable = false)
    private Long booker;
    @Enumerated(EnumType.STRING)
    private BookingState status;

    public Booking() {
        super();
    }
}
