package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public Optional<Item> getItemById(int itemId) {
        try {
            return itemRepository.findById(itemId);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Вещь не найдена");
        }
    }

    @Override
    public List<Item> getAllOwnerItems(int userId) {
        try {
            return itemRepository.findByOwner_Id(userId);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Вещи не найдены");
        }
    }

    @Override
    public List<Item> search(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        } else {
            return itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text);
        }
    }

    @Override
    public Item createItem(int userId, Item item) {
        if (item.getOwner() == null && item.getId() == 0) {
            item.setOwner(userService.getUserById(userId).get());
            return itemRepository.save(item);
        } else {
            throw new NotFoundException("Ошибка входящих данных");
        }
    }

    @Override
    public Item updateItem(int itemId, int userId, Item item) {
        try {
            Item itemFromDb = getItemById(itemId).get();
            if (itemFromDb.getOwner().getId() == userId) {
                if (item.getName() != null) {
                    itemFromDb.setName(item.getName());
                }
                if (item.getDescription() != null) {
                    itemFromDb.setDescription(item.getDescription());
                }
                if (item.getAvailable() != null) {
                    itemFromDb.setAvailable(item.getAvailable());
                }
                itemFromDb.setOwner(userService.getUserById(userId).get());
                return itemRepository.save(itemFromDb);
            } else {
                throw new NotFoundException("Ошибка входящих данных");
            }
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Вещь не найдена");
        }
    }
}