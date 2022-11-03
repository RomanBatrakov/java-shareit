package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer>{
//    List<Item> findAllByOwner_Id(int userId);
//    List<Item> findByNameOrDescriptionContainingIgnoreCase(String text);
}