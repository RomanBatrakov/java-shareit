package ru.practicum.shareit.item.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    Page<Item> findByOwner_Id(int userId, Pageable pageable);

    Page<Item> findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(String name, String description,
                                                                           Pageable pageable);

    List<Item> findByRequest_Id(int requestId);
}