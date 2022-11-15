package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    List<ItemRequestDto> getAllRequests(int userId);

    ItemRequestDto getRequest(int userId, int requestId);

    List<ItemRequestDto> getAllUsersRequests(int userId, Pageable pageable);

    ItemRequestDto createRequest(int userId, ItemRequestDto itemRequestDto);

    ItemRequest findById(int requestId);
}