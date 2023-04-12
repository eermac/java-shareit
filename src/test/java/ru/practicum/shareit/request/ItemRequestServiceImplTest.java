package ru.practicum.shareit.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ItemRequestServiceImplTest {
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

    private Booking booking;
    private User user;
    private Item item;
    private ItemRequest itemRequest;

    @BeforeEach
    private void bookingCreate() {
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
        booking = bookingRepository.save(new Booking(null,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                item, user, BookingState.WAITING));
        itemRequest = itemRequestRepository.save(new ItemRequest(null,
                "tesf",
                user,
                LocalDateTime.now(),
                null));
    }

    @Test
    void add() {
        ItemRequest itemRequest1 = itemRequestService.add(itemRequest, user.getId());

        assertEquals(itemRequest1.getDescription(), itemRequest.getDescription());
        assertEquals(itemRequest1.getRequestor().getName(), itemRequest.getRequestor().getName());
    }

    @Test
    void addWrongDescription() {
        itemRequest.setDescription(null);
        try{
            ItemRequest itemRequest1 = itemRequestService.add(itemRequest, user.getId());
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        }
    }

    @Test
    void getMyRequests() {
        List<ItemRequestDto> itemRequest1 = itemRequestService.getMyRequests(user.getId());

        assertTrue(itemRequest1.size() > 0);
    }

    @Test
    void getMyRequestsWrongUser() {
        try{
            List<ItemRequestDto> itemRequest1 = itemRequestService.getMyRequests(100L);
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        }
    }

    @Test
    void getAllItemRequestsOtherUsers() {
        List<ItemRequestDto> itemRequest1 = itemRequestService.getAllItemRequestsOtherUsers(item.getOwner().getId(),
                0,
                1);

        assertTrue(!(itemRequest1.size() > 0));
    }

    @Test
    void getAllItemRequestsOtherUsersRequest() {
        List<Item> itemList = new ArrayList<>();
        Item item2 = itemRepository.save(new Item(null, "sdfsd", "dsb", true, user, null));
        itemList.add(item2);
        User user2 = userRepository.save(new User(null, "ijof", "erma@mail.ru"));
        ItemRequest itemRequest2 = itemRequestRepository.save(new ItemRequest(null,
                "sdfe",
                user2,
                LocalDateTime.now(),
                itemList));
        item2.setRequestId(itemRequest2.getId());
        itemRepository.save(item2);
        List<ItemRequestDto> itemRequest1 = itemRequestService.getAllItemRequestsOtherUsers(item2.getOwner().getId(),
                0,
                1);
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        assertTrue((itemRequest1.size() > 0));
    }

    @Test
    void getAllItemRequestsOtherUsersWrongPage() {
        try{
            List<ItemRequestDto> itemRequest1 = itemRequestService.getAllItemRequestsOtherUsers(item.getOwner().getId(),
                    -110,
                    1);
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        }
    }

    @Test
    void getRequest() {
        ItemRequestDto itemRequest1 = itemRequestService.getRequest(user.getId(), itemRequest.getId());

        assertEquals(itemRequest1.getDescription(), itemRequest.getDescription());
    }

    @Test
    void getRequestWrongUser() {
        try{
            ItemRequestDto itemRequest1 = itemRequestService.getRequest(100L, 1000L);
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        }
    }

    @Test
    void itemRequestMapTest() {
        PageRequest page = PageRequest.of(1 > 0 ? 1 / 1 : 0, 1);
        Iterable<ItemRequest> itemRequests = itemRequestRepository.findAllByIdOrderByCreated(1L, page);
        List<ItemRequestDto> itemRequests1 = ItemRequestMapper.itemRequestMap(itemRequests);

        assertTrue(itemRequests1.isEmpty());
    }

    @AfterEach
    private void deleteAll() {
        itemRequestRepository.deleteAll();
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }
}
