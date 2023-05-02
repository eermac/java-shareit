package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ItemMapperTest {
    @Autowired
    private ItemRequestService itemRequestService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Test
    void itemDtoMap() {
        User user = userRepository.save(new User(null,
                "name",
                "email@ya.ru"));
        User userOwner = userRepository.save(new User(null,
                "nam3e",
                "em3ail@ya.ru"));
        Item item = itemRepository.save(new Item(null,
                "itemName",
                "description",
                true,
                userOwner,
                null));
        Booking booking = bookingRepository.save(new Booking(null,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                item, user, BookingState.WAITING));
        Booking booking2 = bookingRepository.save(new Booking(null,
                LocalDateTime.now().minusDays(4),
                LocalDateTime.now().minusDays(2),
                item, user, BookingState.APPROVED));
        Booking booking3 = bookingRepository.save(new Booking(null,
                LocalDateTime.now().minusDays(4),
                LocalDateTime.now().minusDays(2),
                item, user, BookingState.APPROVED));

        Comment comment = commentRepository.save(new Comment(null, "text", item, user));
        Comment comment2 = commentRepository.save(new Comment(null, "text2", item, user));
        Comment comment3 = commentRepository.save(new Comment(null, "text3", item, user));

        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);
        bookingList.add(booking2);
        bookingList.add(booking3);

        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        commentList.add(comment2);
        commentList.add(comment3);

        ItemDto itemDto = ItemMapper.itemDtoMap(item, bookingList, commentList);

        assertEquals(itemDto.getName(), "itemName");
    }

    @Test
    void itemDtoMapEmpty() {
        User user = userRepository.save(new User(null,
                "name",
                "email@ya.ru"));
        User userOwner = userRepository.save(new User(null,
                "nam3e",
                "em3ail@ya.ru"));
        Item item = itemRepository.save(new Item(null,
                "itemName",
                "description",
                true,
                userOwner,
                null));

        Comment comment = commentRepository.save(new Comment(null, "text", item, user));
        Comment comment2 = commentRepository.save(new Comment(null, "text2", item, user));
        Comment comment3 = commentRepository.save(new Comment(null, "text3", item, user));

        List<Booking> bookingList = new ArrayList<>();

        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        commentList.add(comment2);
        commentList.add(comment3);

        ItemDto itemDto = ItemMapper.itemDtoMap(item, bookingList, commentList);

        assertEquals(itemDto.getName(), "itemName");
    }

    @Test
    void itemMap() {
        Item item = itemRepository.save(new Item(null,
                "itemName",
                "description",
                true,
                null,
                null));
        ItemDto itemDto = new ItemDto();
        itemDto.setName("fff");
        itemDto.setDescription("fffd");
        itemDto.setAvailable(true);

        ItemMapper.itemMap(item, itemDto);

        assertEquals(item.getDescription(), "fffd");
    }

    @Test
    void itemDtoMap4() {
        User user = userRepository.save(new User(null,
                "name",
                "email@ya.ru"));
        User userOwner = userRepository.save(new User(null,
                "nam3e",
                "em3ail@ya.ru"));
        Item item = itemRepository.save(new Item(null,
                "itemName",
                "description",
                true,
                userOwner,
                null));
        Booking booking = bookingRepository.save(new Booking(null,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                item, user, BookingState.WAITING));
        Booking booking2 = bookingRepository.save(new Booking(null,
                LocalDateTime.now().minusDays(4),
                LocalDateTime.now().minusDays(2),
                item, user, BookingState.APPROVED));
        Booking booking3 = bookingRepository.save(new Booking(null,
                LocalDateTime.now().minusDays(4),
                LocalDateTime.now().minusDays(2),
                item, user, BookingState.APPROVED));
        Booking booking4 = bookingRepository.save(new Booking(null,
                LocalDateTime.now().minusDays(4),
                LocalDateTime.now().minusDays(2),
                item, user, BookingState.APPROVED));
        Booking booking5 = bookingRepository.save(new Booking(null,
                LocalDateTime.now().minusDays(4),
                LocalDateTime.now().minusDays(2),
                item, user, BookingState.APPROVED));

        Comment comment = commentRepository.save(new Comment(null, "text", item, user));
        Comment comment2 = commentRepository.save(new Comment(null, "text2", item, user));
        Comment comment3 = commentRepository.save(new Comment(null, "text3", item, user));

        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);
        bookingList.add(booking2);
        bookingList.add(booking3);
        bookingList.add(booking4);
        bookingList.add(booking5);

        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        commentList.add(comment2);
        commentList.add(comment3);

        ItemDto itemDto = ItemMapper.itemDtoMap(item, bookingList, commentList);

        assertEquals(itemDto.getName(), "itemName");
    }

    @AfterEach
    private void deleteAll() {
        commentRepository.deleteAll();
        itemRequestRepository.deleteAll();
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

}
