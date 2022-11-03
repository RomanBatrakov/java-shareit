package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemRepositoryCustom {
    Item updateItem(Item itemFromDb, Item item, User user);

    Item createItem(User user, Item item);

    List<Item> findByOwner(int userId);
    List<Item> findByNameOrDescriptionContainingIgnoreCase(String text);
}
