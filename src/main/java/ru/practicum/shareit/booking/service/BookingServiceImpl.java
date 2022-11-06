package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingToUserDto;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.ValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static ru.practicum.shareit.booking.BookingStatus.*;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public Booking createBooking(int userId, BookingToUserDto bookingToUserDto) {
        User user = userService.getUserById(userId).get();
        Item item = itemService.getItemById(bookingToUserDto.getItemId()).get();
        if (bookingToUserDto.getStart().isBefore(bookingToUserDto.getEnd()) && item.getAvailable()) {
            Booking booking = Booking.builder()
                    .start(bookingToUserDto.getStart())
                    .end(bookingToUserDto.getEnd())
                    .item(item)
                    .booker(user)
                    .status(WAITING)
                    .build();
            return bookingRepository.save(booking);
        } else {
            throw new IllegalArgumentException("Ошибка входящих данных");
        }
    }

    @Override
    public Booking updateBooking(int bookingId, int userId, Boolean approved) {
        userService.getUserById(userId).get();
        Booking booking = getBookingById(userId, bookingId);
        if (userId == booking.getItem().getOwner().getId()) {
            BookingStatus status = (approved) ? APPROVED : REJECTED;
            booking.setStatus(status);
            return booking;
        } else {
            throw new ValidationException("Ошибка валидации запроса");
        }
    }

    @Override
    public Booking getBookingById(int userId, int bookingId) {
        try {
            Booking booking = bookingRepository.findById(bookingId).get();
            if (userId == booking.getBooker().getId() || userId == booking.getItem().getOwner().getId()) {
                return booking;
            } else {
                throw new ValidationException("Ошибка валидации запроса");
            }
        } catch (NotFoundException e) {
            throw new NotFoundException("Бронирование не найдено");
        }
    }

    @Override
    public List<Booking> getUserBookings(int userId, BookingState state) {
        try {
            switch (state) {
                case ALL:
                    return bookingRepository.findByBooker_IdOrderByStartDesc(userId);
                case CURRENT:
                    return bookingRepository.findByBooker_IdAndEndAfterAndStartBeforeOrderByStartDesc(userId,
                            LocalDateTime.now(), LocalDateTime.now());
                case PAST:
                    return bookingRepository.findByBooker_IdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
                case FUTURE:
                    return bookingRepository.findByBooker_IdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
                case WAITING:
                    return bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId, WAITING);
                case REJECTED:
                    return bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId, REJECTED);
                default:
                    throw new ValidationException("Ошибка валидации запроса");
            }
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Бронирования не найдены");
        }
    }
}