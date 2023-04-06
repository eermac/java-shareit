package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select i from Item i where i.owner.id = ?1 order by i.id")
    List<Item> itemOwnerSearch(Long ownerId);

    @Query("select i " +
            "from Item i " +
            "where (upper(i.name) like upper(concat('%', ?1, '%')) " +
            "  or upper(i.description) like upper(concat('%', ?1, '%'))) and i.available != false")
    List<Item> itemSearch(String text);
}
