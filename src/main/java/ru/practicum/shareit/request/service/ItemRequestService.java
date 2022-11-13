package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    List<ItemRequestDto> getAllRequests(int userId);

    ItemRequestDto getRequest(int userId, int requestId);

    Page<ItemRequestDto> getAllUsersRequests(int userId, Pageable pageable);

    ItemRequestDto createRequest(int userId, ItemRequestDto itemRequestDto);
}