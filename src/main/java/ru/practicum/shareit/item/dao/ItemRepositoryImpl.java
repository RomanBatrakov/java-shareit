package ru.practicum.shareit.item.dao;

import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ItemRepositoryImpl implements ItemRepository {

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
            findById(itemId).get().setName(item.getName());
        } else {
            item.setName(findById(itemId).get().getName());
        }
        if (item.getDescription() != null) {
            findById(itemId).get().setDescription(item.getDescription());
        } else {
            item.setDescription(findById(itemId).get().getDescription());
        }
        if (item.getAvailable() != null) {
            findById(itemId).get().setAvailable(item.getAvailable());
        } else {
            item.setAvailable(findById(itemId).get().getAvailable());
        }
        item.setRequest(findById(itemId).get().getRequest());
        item.setOwner(user);
        item.setId(itemId);
        return item;
    }

    @Override
    public void deleteOwnerItems(int userId) {
        items.removeIf(item -> item.getOwner().getId() == userId);
    }

}
