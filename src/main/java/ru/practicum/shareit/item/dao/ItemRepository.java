package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> getAllOwnerItems(int userId);

    List<Item> search(String text);

    Item updateItem(int itemId, Item item, User user);

    void deleteOwnerItems(int userId);
}