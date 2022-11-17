package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSimpleDto;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.BookingStatus.*;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingMapper bookingMapper;
    private final UserMapper userMapper;

    @Override
    public BookingDto createBooking(int userId, BookingSimpleDto bookingSimpleDto) {
        User user = userMapper.toUser(userService.getUserById(userId));
        Item item = itemService.getItemById(bookingSimpleDto.getItemId()).get();
        if (item.getOwner().getId() == user.getId()) throw new NotFoundException("Владелец не может бронировать");
        if (bookingSimpleDto.getStart().isBefore(bookingSimpleDto.getEnd()) && item.getAvailable()) {
            Booking booking = Booking.builder()
                    .start(bookingSimpleDto.getStart())
                    .end(bookingSimpleDto.getEnd())
                    .item(item)
                    .booker(user)
                    .status(WAITING)
                    .build();
            return bookingMapper.toBookingDto(bookingRepository.save(booking));
        } else {
            throw new IllegalArgumentException("Ошибка входящих данных");
        }
    }

    @Override
    public BookingDto updateBooking(int bookingId, int userId, Boolean approved) {
        userService.getUserById(userId);
        Booking booking = bookingMapper.toBooking(getBookingById(userId, bookingId));
        if (booking.getStatus().equals(APPROVED)) throw new IllegalArgumentException("Бронирование уже подтверждено");
        if (userId == booking.getItem().getOwner().getId()) {
            BookingStatus status = (approved) ? APPROVED : REJECTED;
            booking.setStatus(status);
            return bookingMapper.toBookingDto(bookingRepository.save(booking));
        } else {
            throw new NotFoundException("Некорректный пользователь");
        }
    }

    @Override
    public BookingDto getBookingById(int userId, int bookingId) {
        userService.getUserById(userId);
        try {
            Booking booking = bookingRepository.findById(bookingId).get();
            if (userId == booking.getBooker().getId() || userId == booking.getItem().getOwner().getId()) {
                return bookingMapper.toBookingDto(booking);
            } else {
                throw new NotFoundException("Ошибка валидации запроса");
            }
        } catch (NotFoundException e) {
            throw new NotFoundException("Бронирование не найдено");
        }
    }

    @Override
    public List<BookingDto> getUserBookings(int userId, String state, Pageable pageable) {
        userService.getUserById(userId);
        Page<Booking> userBookings;
        try {
            BookingState bookingState = BookingState.valueOf(state);
            switch (bookingState) {
                case ALL:
                    userBookings = bookingRepository.findByBooker_IdOrderByStartDesc(userId, pageable);
                    break;
                case CURRENT:
                    userBookings = bookingRepository.findByBooker_IdAndEndAfterAndStartBeforeOrderByStartDesc(userId,
                            LocalDateTime.now(), LocalDateTime.now(), pageable);
                    break;
                case PAST:
                    userBookings = bookingRepository.findByBooker_IdAndEndBeforeOrderByStartDesc(userId,
                            LocalDateTime.now(), pageable);
                    break;
                case FUTURE:
                    userBookings = bookingRepository.findByBooker_IdAndStartAfterOrderByStartDesc(userId,
                            LocalDateTime.now(), pageable);
                    break;
                case WAITING:
                    userBookings = bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId, WAITING,
                            pageable);
                    break;
                case REJECTED:
                    userBookings = bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId, REJECTED,
                            pageable);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown state: " + state);
            }
        } catch (NotFoundException e) {
            throw new NotFoundException("Бронирования не найдены");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
        return userBookings.stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getOwnerBookings(int ownerId, String state, Pageable pageable) {
        userService.getUserById(ownerId);
        Page<Booking> ownerBookings;
        try {
            BookingState bookingState = BookingState.valueOf(state);
            switch (bookingState) {
                case ALL:
                    ownerBookings = bookingRepository.findByItem_OwnerIdOrderByStartDesc(ownerId, pageable);
                    break;
                case CURRENT:
                    ownerBookings = bookingRepository.findByItem_OwnerIdAndEndAfterAndStartBeforeOrderByStartDesc(
                            ownerId, LocalDateTime.now(), LocalDateTime.now(), pageable);
                    break;
                case PAST:
                    ownerBookings = bookingRepository.findByItem_OwnerIdAndEndBeforeOrderByStartDesc(ownerId,
                            LocalDateTime.now(), pageable);
                    break;
                case FUTURE:
                    ownerBookings = bookingRepository.findByItem_OwnerIdAndStartAfterOrderByStartDesc(ownerId,
                            LocalDateTime.now(), pageable);
                    break;
                case WAITING:
                    ownerBookings = bookingRepository.findByItem_OwnerIdAndStatusOrderByStartDesc(ownerId, WAITING,
                            pageable);
                    break;
                case REJECTED:
                    ownerBookings = bookingRepository.findByItem_OwnerIdAndStatusOrderByStartDesc(ownerId, REJECTED,
                            pageable);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown state: " + state);
            }
        } catch (NotFoundException e) {
            throw new NotFoundException("Бронирования не найдены");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
        return ownerBookings.stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> getAllItemBookings(int itemId) {
        return bookingRepository.findByItem_IdOrderByStartDesc(itemId);
    }

    @Override
    public List<Booking> findByItem_IdAndBooker_IdAndStatusAndEndBefore(int itemId, int userId, BookingStatus
            status,
                                                                        LocalDateTime now) {
        return bookingRepository.findByItem_IdAndBooker_IdAndStatusAndEndBefore(itemId, userId, status, now);
    }
}