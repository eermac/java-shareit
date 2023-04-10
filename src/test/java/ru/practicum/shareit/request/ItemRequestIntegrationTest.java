package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class ItemRequestIntegrationTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemService itemService;
    @MockBean
    private UserService userService;
    @MockBean
    private BookingService bookingService;
    @MockBean
    private ItemRequestService itemRequestService;
    @InjectMocks
    private ItemRequestController itemRequestController;

    @SneakyThrows
    @Test
    void add() {
        ItemRequest request = new ItemRequest();
        Long userId = 1L;
        when(itemRequestService.add(request, userId)).thenReturn(request);

        mockMvc.perform(post("/requests")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(itemRequestService, never()).add(request, userId);
    }

    @SneakyThrows
    @Test
    void getMyRequests() {
        long userId = 1L;
        mockMvc.perform(get("/requests", userId)).andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getAllItemRequestsOtherUsers() {
        long userId = 1L;
        mockMvc.perform(get("/requests/all", userId, 0, 0)).andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getRequest() {
        long requestId = 1L;
        long userId = 1L;
        mockMvc.perform(get("/requests/{requestId}", requestId, userId)).andExpect(status().isBadRequest());
    }

}
