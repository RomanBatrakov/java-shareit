package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.Item;

import java.util.List;

public interface ItemService {
    Item getItemById(int itemId);

    List<Item> getAllOwnerItems(int userId);

    List<Item> search(String text);

    Item createItem(int userId, Item item);

    Item updateItem(int itemId, int userId, Item item);
}
