package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
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

    @Override
    public List<ItemRequestDto> getAllRequests(int userId) {
        userService.getUserById(userId);
        try {
            return itemRequestRepository.findByRequestor_Id(userId).stream()
                    .map(itemRequestMapper::toItemRequestDto)
                    .collect(Collectors.toList());
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Запросы не найдены");
        }
    }

    @Override
    public ItemRequestDto getRequest(int userId, int requestId) {
        userService.getUserById(userId);
        try {
            return itemRequestMapper.toItemRequestDto(itemRequestRepository.findById(requestId).get());
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Запрос не найден");
        }
    }

    @Override
    public Page<ItemRequestDto> getAllUsersRequests(int userId, Pageable pageable) {
        User user = userMapper.toUser(userService.getUserById(userId));
        try {
        return itemRequestRepository.findByRequestorNotLike(user, pageable)
                .map(itemRequestMapper::toItemRequestDto);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Запросы не найдены");
        }
    }

    @Override
    public ItemRequestDto createRequest(int userId, ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(userMapper.toUser(userService.getUserById(userId)));
        return itemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }
}