package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Item getItemById(int itemId) {
        try {
            return itemRepository.getItemById(itemId);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Вещь не найдена");
        }
    }

    @Override
    public List<Item> getAllOwnerItems(int userId) {
        try {
            return itemRepository.getAllOwnerItems(userId);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Вещи не найдены");
        }
    }

    @Override
    public List<Item> search(String text) {
        return itemRepository.search(text);
    }

    @Override
    public Item createItem(int userId, Item item) {
        if (itemValidation(0, userId, item)) {
            return itemRepository.createItem(userRepository.getUserById(userId), item);
        } else {
            throw new NotFoundException("Ошибка входящих данных");
        }
    }

    @Override
    public Item updateItem(int itemId, int userId, Item item) {
        try {
            if (itemValidation(itemId, userId, item)) {
                return itemRepository.updateItem(itemId, item, userRepository.getUserById(userId));
            } else {
                throw new NotFoundException("Ошибка входящих данных");
            }
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Вещь не найдена");
        }
    }

    private boolean itemValidation(int itemId, int userId, Item item) {
        boolean ownerValid = false;
        if (itemId > 0) {
            ownerValid = itemRepository.getItemById(itemId).getOwner().getId() == userId;
        } else if (item.getOwner() == null && item.getId() == 0) {
            ownerValid = true;
        }
        boolean userValid = userRepository.getAllUsers().stream()
                .anyMatch(x -> x.getId() == userId);
        return ownerValid && userValid;
    }
}
