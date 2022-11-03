package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.Item;

public interface ItemRepository extends JpaRepository<Item, Integer>, ItemRepositoryCustom {
}