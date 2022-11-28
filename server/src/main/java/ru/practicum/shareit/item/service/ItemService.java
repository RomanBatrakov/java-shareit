package ru.practicum.shareit.item.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    Optional<Item> getItemById(long itemId);

    List<ItemWithBookingsDto> getAllOwnerItems(long userId, Pageable pageable);

    List<ItemDto> search(String text, Pageable pageable);

    ItemDto createItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long itemId, long userId, ItemDto itemDto);

    ItemWithBookingsDto itemConverter(long userId, Item item);

    CommentDto createComment(long itemId, long userId, CommentDto commentDto);

    List<ItemDto> getRequestItems(long requestId);

    ItemWithBookingsDto getItemWithBookingsById(long userId, long itemId);
}