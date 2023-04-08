package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("Select c from Comment c where c.itemId.id = ?1 ")
    List<Comment> searchCommentByItem(Long itemId);
}
