package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Entity
@Table(name = "comments", schema = "public")
@Getter
@AllArgsConstructor
@Setter
@ToString
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item itemId;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User authorId;

    public Comment() {
        super();
    }
}
