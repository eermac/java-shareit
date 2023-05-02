package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceTest {
    @Mock
    private ItemRequestService itemRequestService;
    @InjectMocks
    private ItemRequestController itemRequestController;

    @Test
    void add() {
        ItemRequest request = new ItemRequest();
        Long userId = 1L;
        when(itemRequestService.add(request, userId)).thenReturn(request);

        ResponseEntity<ItemRequest> response = ResponseEntity.ok(itemRequestController.add(request, userId));
        ItemRequest testRequest = itemRequestService.add(request, userId);

        assertEquals(testRequest, request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getMyRequests() {
        List<ItemRequestDto> requestDtoList = List.of(new ItemRequestDto());
        Long userId = 1L;
        when(itemRequestService.getMyRequests(userId)).thenReturn(requestDtoList);

        ResponseEntity<List<ItemRequestDto>> response = ResponseEntity.ok(itemRequestController.getMyRequests(userId));
        List<ItemRequestDto>  testRequest = itemRequestService.getMyRequests(userId);

        assertEquals(requestDtoList, testRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAllItemRequestsOtherUsers() {
        List<ItemRequestDto> requestDtoList = List.of(new ItemRequestDto());
        Long userId = 1L;
        when(itemRequestService.getAllItemRequestsOtherUsers(userId, 0, 1)).thenReturn(requestDtoList);

        ResponseEntity<List<ItemRequestDto>> response = ResponseEntity.ok(itemRequestController.getAllItemRequestsOtherUsers(userId, "0", "1"));
        List<ItemRequestDto>  testRequest = itemRequestService.getAllItemRequestsOtherUsers(userId, 0, 1);

        assertEquals(requestDtoList, testRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getRequest() {
        ItemRequestDto requestDto = new ItemRequestDto();
        Long userId = 1L;
        Long requestId = 1L;
        when(itemRequestService.getRequest(userId, requestId)).thenReturn(requestDto);

        ResponseEntity<ItemRequestDto> response = ResponseEntity.ok(itemRequestController.getRequest(userId, requestId));
        ItemRequestDto  testRequest = itemRequestService.getRequest(userId, requestId);

        assertEquals(requestDto, testRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
