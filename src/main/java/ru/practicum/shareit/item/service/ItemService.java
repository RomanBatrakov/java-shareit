package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    Optional<Item> getItemById(int itemId);

    List<ItemWithBookingsDto> getAllOwnerItems(int userId);

    List<Item> search(String text);

    Item createItem(int userId, Item item);

    Item updateItem(int itemId, int userId, Item item);

    ItemWithBookingsDto itemConverter(int userId, Item item);

    CommentDto createComment(int itemId, int userId, Comment comment);
}