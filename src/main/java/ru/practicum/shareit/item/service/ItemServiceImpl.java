package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Item> getItemById(int itemId) {
        try {
            return itemRepository.findById(itemId);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Вещь не найдена");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> getAllOwnerItems(int userId) {
        try {
            return itemRepository.findByOwner(userId);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Вещи не найдены");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> search(String text) {
        return itemRepository.findByNameOrDescriptionContainingIgnoreCase(text);
    }

    @Override
    @Transactional
    public Item createItem(int userId, Item item) {
        if (item.getOwner() == null && item.getId() == 0) {
            return itemRepository.createItem(userRepository.findById(userId).get(), item);
        } else {
            throw new NotFoundException("Ошибка входящих данных");
        }
    }

    @Override
    @Transactional
    public Item updateItem(int itemId, int userId, Item item) {
        try {
            Item itemFromDb = getItemById(itemId).get();
            if (itemFromDb.getOwner().getId() == userId) {
                return itemRepository.updateItem(itemFromDb, item, userRepository.findById(userId).get());
            } else {
                throw new NotFoundException("Ошибка входящих данных");
            }
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Вещь не найдена");
        }
    }
}