package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.service.UserService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class BookingIntegrationTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private BookingService bookingService;
    @MockBean
    private ItemRequestService itemRequestService;
    @MockBean
    private ItemService itemService;
    @InjectMocks
    private UserController userController;

    @SneakyThrows
    @Test
    void add() {
        Booking booking = new Booking();
        BookingDto bookingDto = new BookingDto();
        Long userId = 1L;
        when(bookingService.add(bookingDto, userId)).thenReturn(booking);

        mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).add(bookingDto, userId);
    }

    @SneakyThrows
    @Test
    void getBooking() {
        long bookingId = 1L;
        long userId = 1L;
        mockMvc.perform(get("/bookings/{bookingId}", bookingId, userId)).andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getAllBookings() {
        long userId = 1L;
        mockMvc.perform(get("/bookings", userId)).andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getAllBookingsOwner() {
        long userId = 1L;
        mockMvc.perform(get("/bookings/owner", userId)).andExpect(status().isBadRequest());
    }
}
