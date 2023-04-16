package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    @Query("Select r from ItemRequest r where r.requestor.id = ?1")
    List<ItemRequest> searchRequestByUser(Long userId);

    Page<ItemRequest> findAllByIdOrderByCreated(Long id, Pageable page);
}
