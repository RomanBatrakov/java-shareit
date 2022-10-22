package ru.practicum.shareit.item.dao;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
public class ItemRepositoryImpl implements ItemRepository {
    private int lastId = 0;
    private final List<Item> items = new ArrayList<>();

    @Override
    public Item getItemById(int itemId) {
        return items.stream()
                .filter(x -> x.getId() == itemId)
                .findFirst()
                .get();
    }

    @Override
    public List<Item> getAllOwnerItems(int userId) {
        return items.stream()
                .filter(x -> x.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> search(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        } else {
            return items.stream()
                    .filter(x -> x.getName().toLowerCase().contains(text.toLowerCase())
                            || x.getDescription().toLowerCase().contains(text.toLowerCase()))
                    .filter(Item::getAvailable)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Item createItem(User user, Item item) {
        item.setId(getId());
        item.setOwner(user);
        items.add(item);
        return item;
    }

    @Override
    public Item updateItem(int itemId, Item item, User user) {
        if (item.getName() != null) {
            getItemById(itemId).setName(item.getName());
        } else {
            item.setName(getItemById(itemId).getName());
        }
        if (item.getDescription() != null) {
            getItemById(itemId).setDescription(item.getDescription());
        } else {
            item.setDescription(getItemById(itemId).getDescription());
        }
        if (item.getAvailable() != null) {
            getItemById(itemId).setAvailable(item.getAvailable());
        } else {
            item.setAvailable(getItemById(itemId).getAvailable());
        }
        item.setRequest(getItemById(itemId).getRequest());
        item.setOwner(user);
        item.setId(itemId);
        return item;
    }

    @Override
    public void deleteOwnerItems(int userId) {
        items.removeIf(item -> item.getOwner().getId() == userId);
    }

    private int getId() {
        return ++lastId;
    }
}
