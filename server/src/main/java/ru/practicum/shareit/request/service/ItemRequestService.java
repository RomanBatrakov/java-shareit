package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    List<ItemRequestDto> getAllRequests(long userId);

    ItemRequestDto getRequest(long userId, long requestId);

    List<ItemRequestDto> getAllUsersRequests(long userId, Pageable pageable);

    ItemRequestDto createRequest(long userId, ItemRequestDto itemRequestDto);

    ItemRequest findById(long requestId);
}