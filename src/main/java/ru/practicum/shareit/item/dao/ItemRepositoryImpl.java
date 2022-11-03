package ru.practicum.shareit.item.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepositoryCustom {
    private final ItemRepository itemRepository;

    public ItemRepositoryImpl(@Lazy ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<Item> findByOwner(int userId) {
        return itemRepository.findByOwner(userId);
//        return itemRepository.findAll().stream()
//                .filter(x -> x.getOwner().getId() == userId)
//                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findByNameOrDescriptionContainingIgnoreCase(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        } else {
            return itemRepository.findByNameOrDescriptionContainingIgnoreCase(text);
        }
//            return itemRepository.findAll().stream()
//                    .filter(x -> x.getName().toLowerCase().contains(text.toLowerCase())
//                            || x.getDescription().toLowerCase().contains(text.toLowerCase()))
//                    .filter(Item::getAvailable)
//                    .collect(Collectors.toList());
//        }
    }

    @Override
    public Item createItem(User user, Item item) {
        item.setOwner(user);
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Item itemFromDb, Item item, User user) {
        if (item.getName() != null) {
            itemFromDb.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemFromDb.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemFromDb.setAvailable(item.getAvailable());
        }
        itemFromDb.setOwner(user);
        return itemRepository.save(itemFromDb);
    }
}