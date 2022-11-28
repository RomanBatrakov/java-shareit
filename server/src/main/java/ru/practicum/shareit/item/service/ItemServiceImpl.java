package ru.practicum.shareit.item.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingSimpleDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
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
    private final UserMapper userMapper;
    private final CommentRepository commentRepository;
    private final ItemRequestService itemRequestService;

    public ItemServiceImpl(ItemRepository itemRepository, UserService userService, @Lazy BookingService bookingService,
                           ItemMapper itemMapper, BookingMapper bookingMapper, CommentMapper commentMapper,
                           UserMapper userMapper, CommentRepository commentRepository,
                           @Lazy ItemRequestService itemRequestService) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.bookingService = bookingService;
        this.itemMapper = itemMapper;
        this.bookingMapper = bookingMapper;
        this.commentMapper = commentMapper;
        this.userMapper = userMapper;
        this.commentRepository = commentRepository;
        this.itemRequestService = itemRequestService;
    }

    @Override
    public Optional<Item> getItemById(int itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isPresent()) {
            return item;
        } else {
            throw new NotFoundException("Вещь не найдена");
        }
    }

    @Override
    public List<ItemWithBookingsDto> getAllOwnerItems(int userId, Pageable pageable) {
        return itemRepository.findByOwner_Id(userId, pageable).stream()
                .map(x -> itemConverter(userId, x))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text, Pageable pageable) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        } else {
            return itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text, pageable)
                    .stream()
                    .map(itemMapper::toItemDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public ItemDto createItem(int userId, ItemDto itemDto) {
        ItemRequest itemRequest = null;
        Item item = itemMapper.toItem(itemDto);
        if (itemDto.getRequestId() != null) {
            itemRequest = itemRequestService.findById(itemDto.getRequestId());
        }
        if (item.getOwner() == null && item.getId() == 0) {
            item.setOwner(userMapper.toUser(userService.getUserById(userId)));
            item.setRequest(itemRequest);
            ItemDto newItemDto = itemMapper.toItemDto(itemRepository.save(item));
            newItemDto.setRequestId(itemDto.getRequestId());
            return newItemDto;
        } else {
            throw new NotFoundException("Ошибка входящих данных");
        }
    }

    @Override
    public ItemDto updateItem(int itemId, int userId, ItemDto itemDto) {
        Item item = itemMapper.toItem(itemDto);
        Item itemFromDb = getItemById(itemId).get();
        if (itemFromDb.getOwner().getId() == userId) {
            Optional.ofNullable(item.getName()).ifPresent(itemFromDb::setName);
            Optional.ofNullable(item.getDescription()).ifPresent(itemFromDb::setDescription);
            Optional.ofNullable(item.getAvailable()).ifPresent(itemFromDb::setAvailable);
            itemFromDb.setOwner(userMapper.toUser(userService.getUserById(userId)));
            return itemMapper.toItemDto(itemRepository.save(itemFromDb));
        } else {
            throw new NotFoundException("Ошибка входящих данных");
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
    public CommentDto createComment(int itemId, int userId, CommentDto commentDto) {
        Item item = getItemById(itemId).get();
        User user = userMapper.toUser(userService.getUserById(userId));
        if (!bookingService.findByItem_IdAndBooker_IdAndStatusAndEndBefore(itemId,
                userId, BookingStatus.APPROVED, LocalDateTime.now()).isEmpty()) {
            Comment comment = commentMapper.toComment(commentDto);
            comment.setAuthor(user);
            comment.setItem(item);
            comment.setCreated(LocalDateTime.now());
            CommentDto newCommentDto = commentMapper.toCommentDto(commentRepository.save(comment));
            newCommentDto.setAuthorName(user.getName());
            return newCommentDto;
        } else {
            throw new IllegalArgumentException("Ошибка входящих данных");
        }
    }

    @Override
    public List<ItemDto> getRequestItems(int requestId) {
        return itemRepository.findByRequest_Id(requestId).stream()
                .map(itemMapper::toItemDto)
                .peek(x -> x.setRequestId(requestId))
                .collect(Collectors.toList());
    }

    @Override
    public ItemWithBookingsDto getItemWithBookingsById(int userId, int itemId) {
        return itemConverter(userId, getItemById(itemId).get());
    }
}