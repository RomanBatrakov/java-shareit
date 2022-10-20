package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemRepository {
    Item getItemById(int itemId);

    List<Item> getAllOwnerItems(int userId);

    List<Item> search(String text);

    Item createItem(User user, Item item);

    Item updateItem(int itemId, Item item, User user);

    void deleteOwnerItems(int userId);
}
