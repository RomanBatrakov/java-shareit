package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer>{
    List<Item> findByOwner_Id(int userId);
    List<Item> findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(String name, String description);
}