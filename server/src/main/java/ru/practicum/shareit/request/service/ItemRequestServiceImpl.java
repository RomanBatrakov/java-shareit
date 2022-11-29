package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestMapper itemRequestMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemService itemService;

    @Override
    public List<ItemRequestDto> getAllRequests(long userId) {
        userService.getUserById(userId);
        return itemRequestRepository.findByRequestor_Id(userId).stream()
                .map(itemRequestMapper::toItemRequestDto)
                .peek(x -> x.setItems(itemService.getRequestItems(x.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getRequest(long userId, long requestId) {
        userService.getUserById(userId);
        ItemRequestDto itemRequestDto = itemRequestMapper.toItemRequestDto(findById(requestId));
        itemRequestDto.setItems(itemService.getRequestItems(requestId));
        return itemRequestDto;
    }

    @Override
    public List<ItemRequestDto> getAllUsersRequests(long userId, Pageable pageable) {
        User user = userMapper.toUser(userService.getUserById(userId));
        return itemRequestRepository.findByRequestorIsNot(user, pageable).stream()
                .map(itemRequestMapper::toItemRequestDto)
                .peek(x -> x.setItems(itemService.getRequestItems(x.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto createRequest(long userId, ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(userMapper.toUser(userService.getUserById(userId)));
        itemRequestDto = itemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
        itemRequestDto.setItems(new ArrayList<>());
        return itemRequestDto;
    }

    public ItemRequest findById(long requestId) {
        try {
            return itemRequestRepository.findById(requestId).get();
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Запрос не найден");
        }
    }
}