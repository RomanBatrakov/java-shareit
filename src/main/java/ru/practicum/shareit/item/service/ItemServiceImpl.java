package ru.practicum.shareit.item.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingToUserDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingService bookingService;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;

    public ItemServiceImpl(ItemRepository itemRepository, UserService userService, @Lazy BookingService bookingService, ItemMapper itemMapper, BookingMapper bookingMapper) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.bookingService = bookingService;
        this.itemMapper = itemMapper;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public Optional<Item> getItemById(int itemId) {
        try {
            return itemRepository.findById(itemId);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Вещь не найдена");
        }
    }

    @Override
    public List<ItemWithBookingsDto> getAllOwnerItems(int userId) {
        try {
            return itemRepository.findByOwner_Id(userId).stream()
                    .map(x -> itemConverter(userId, x))
                    .collect(Collectors.toList());
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

    @Override
    public ItemWithBookingsDto itemConverter(int userId, Item item) {
        BookingToUserDto lastBooking = null;
        BookingToUserDto nextBooking = null;
        List<Booking> itemBookings = bookingService.getAllItemBookings(item.getId());
        List<Booking> lastBookings = itemBookings.stream()
                .filter(x -> x.getEnd().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
        if (!lastBookings.isEmpty() && item.getOwner().getId() == userId) {
            lastBooking = bookingMapper.toBookingToUserDto(lastBookings.get(0));
            lastBooking.setBookerId(lastBookings.get(0).getBooker().getId());
            lastBooking.setItemId(lastBookings.get(0).getItem().getId());
        }
        List<Booking> nextBookings = itemBookings.stream()
                .filter(x -> x.getStart().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
        Collections.reverse(nextBookings);
        if (!nextBookings.isEmpty() && item.getOwner().getId() == userId) {
            nextBooking = bookingMapper.toBookingToUserDto(nextBookings.get(0));
            nextBooking.setBookerId(nextBookings.get(0).getBooker().getId());
            nextBooking.setItemId(nextBookings.get(0).getItem().getId());
        }
        ItemWithBookingsDto itemWithBookingsDto = itemMapper.toItemWithBookingsDto(item);
        itemWithBookingsDto.setLastBooking(lastBooking);
        itemWithBookingsDto.setNextBooking(nextBooking);
        return itemWithBookingsDto;
    }
}