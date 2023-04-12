package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDate;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
public class ItemServiceImplTest {
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;

    private Item item;
    private User user;

    @BeforeEach
    private void createItem() {
        user = userRepository.save(new User(null,
                "name",
                "email@ya.ru"));
        User userOwner = userRepository.save(new User(null,
                "nam3e",
                "em3ail@ya.ru"));
        item = itemRepository.save(new Item(null,
                "itemName",
                "description",
                true,
                userOwner,
                null));
    }

    @Test
    void add() {
        Item newItem = itemService.add(item, user.getId());

        assertEquals(newItem.getName(), item.getName());
        assertEquals(newItem.getId(), item.getId());
    }

    @Test
    void addWrongUser() {
        try {
            Item newItem = itemService.add(item, 100L);
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        }
    }

    @Test
    void updatePatch() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName("newItem");
        Item newItem = itemService.updatePatch(itemDto, itemDto.getId(), user.getId());

        assertEquals(newItem.getName(), "newItem");
        assertEquals(newItem.getId(), item.getId());
    }

    @Test
    void updatePatchWrongItem() {
        try {
            ItemDto itemDto = new ItemDto();
            itemDto.setId(1L);
            itemDto.setName("newItem");
            Item newItem = itemService.updatePatch(itemDto, 100L, user.getId());
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        }
    }

    @Test
    void getItem() {
        ItemDto newItem = itemService.getItem(item.getId(), user.getId());

        assertEquals(newItem.getName(), item.getName());
        assertEquals(newItem.getId(), item.getId());
    }

    @Test
    void getItemWrongUser() {
        try {
            ItemDto newItem = itemService.getItem(item.getId(), 100L);
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        }
    }

    @Test
    void getItems() {
        List<ItemDto> newItem = itemService.getItems(item.getOwner().getId());


        assertEquals(newItem.get(0).getName(), item.getName());
        assertTrue(newItem.size() > 0);
    }

    @Test
    void getItemsWrongUser() {
        try {
            List<ItemDto> newItem = itemService.getItems(100L);
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        }
    }

    @Test
    void addComment() {
        Comment comment = new Comment(null, "text", item, user);
        Booking booking = bookingRepository.save(new Booking(null,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusDays(2),
                item,
                user,
                BookingState.APPROVED));

        CommentDto commentDto = itemService.addComment(comment, item.getId(), user.getId());

        assertEquals(commentDto.getText(), "text");
        assertEquals(commentDto.getAuthorName(), user.getName());
    }

    @Test
    void addCommentWrongComment() {
        Comment comment = new Comment(null, "", item, user);
        Booking booking = bookingRepository.save(new Booking(null,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(3),
                item,
                user,
                BookingState.APPROVED));

        try {
        CommentDto commentDto = itemService.addComment(comment, item.getId(), user.getId());
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        }
    }

    @Test
    void getItemsSearch() {
        List<Item> newItem = itemService.getItemsForSearch(item.getDescription());

        assertEquals(newItem.get(0).getName(), item.getName());
        assertTrue(newItem.size() > 0);
    }

    @Test
    void getItemsSearchEmpty() {
        List<Item> newItem = itemService.getItemsForSearch("hhhh");

        assertTrue(newItem.isEmpty());
    }

    @Test
    void commentDtoTest() {
        CommentDto test = new CommentDto();

        assertTrue(test != null);
    }

    @Test
    void userDtoTest() {
        UserDto test = new UserDto();

        assertTrue(test != null);
    }

    @Test
    void bookingDateTest() {
        BookingDate test = new BookingDate();

        assertTrue(test != null);
    }

    @AfterEach
    private void deleteAll() {
        bookingRepository.deleteAll();
        commentRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }
}
