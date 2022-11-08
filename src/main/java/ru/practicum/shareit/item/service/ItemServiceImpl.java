package ru.practicum.shareit.item.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingSimpleDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.user.User;
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
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserService userService, @Lazy BookingService bookingService, ItemMapper itemMapper, BookingMapper bookingMapper, CommentMapper commentMapper, CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.bookingService = bookingService;
        this.itemMapper = itemMapper;
        this.bookingMapper = bookingMapper;
        this.commentMapper = commentMapper;
        this.commentRepository = commentRepository;
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
                if (item.getName() != null)
                    itemFromDb.setName(item.getName());
                if (item.getDescription() != null)
                    itemFromDb.setDescription(item.getDescription());
                if (item.getAvailable() != null)
                    itemFromDb.setAvailable(item.getAvailable());
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
        List<Booking> itemBookings = bookingService.getAllItemBookings(item.getId());
        ItemWithBookingsDto itemWithBookingsDto = itemMapper.toItemWithBookingsDto(item);
        itemWithBookingsDto.setLastBooking(findLastBooking(userId, item, itemBookings));
        itemWithBookingsDto.setNextBooking(findNextBooking(userId, item, itemBookings));
        List<CommentDto> comments = findComments(itemWithBookingsDto);
        itemWithBookingsDto.setComments(comments);
        return itemWithBookingsDto;
    }

    private BookingSimpleDto findNextBooking(int userId, Item item, List<Booking> itemBookings) {
        List<Booking> nextBookings = itemBookings.stream()
                .filter(x -> x.getStart().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
        Collections.reverse(nextBookings);
        return getBookingSimpleDto(userId, item, nextBookings);
    }

    private BookingSimpleDto findLastBooking(int userId, Item item, List<Booking> itemBookings) {
        List<Booking> lastBookings = itemBookings.stream()
                .filter(x -> x.getEnd().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
        return getBookingSimpleDto(userId, item, lastBookings);
    }

    private BookingSimpleDto getBookingSimpleDto(int userId, Item item, List<Booking> bookingLists) {
        BookingSimpleDto booking = null;
        if (!bookingLists.isEmpty() && item.getOwner().getId() == userId) {
            booking = bookingMapper.toBookingSimpleDto(bookingLists.get(0));
            booking.setBookerId(bookingLists.get(0).getBooker().getId());
            booking.setItemId(bookingLists.get(0).getItem().getId());
        }
        return booking;
    }

    private List<CommentDto> findComments(ItemWithBookingsDto itemWithBookingsDto) {
        return commentRepository.findByItem_Id(itemWithBookingsDto.getId()).stream()
                .map(c -> new CommentDto(
                        c.getId(),
                        c.getText(),
                        c.getAuthor().getName(),
                        c.getCreated()))
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto createComment(int itemId, int userId, Comment comment) {
        Item item = getItemById(itemId).get();
        User user = userService.getUserById(userId).get();
        if (!bookingService.findByItem_IdAndBooker_IdAndStatusAndEndBefore(itemId,
                userId, BookingStatus.APPROVED, LocalDateTime.now()).isEmpty()) {
            comment.setAuthor(user);
            comment.setItem(item);
            comment.setCreated(LocalDateTime.now());
            CommentDto commentDto = commentMapper.toCommentDto(commentRepository.save(comment));
            commentDto.setAuthorName(user.getName());
            return commentDto;
        } else {
            throw new IllegalArgumentException("Ошибка входящих данных");
        }
    }
}