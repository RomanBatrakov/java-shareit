package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingSimpleDto;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.booking.BookingStatus.*;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public Booking createBooking(int userId, BookingSimpleDto bookingToUserDto) {
        User user = userService.getUserById(userId).get();
        Item item = itemService.getItemById(bookingToUserDto.getItemId()).get();
        if (item.getOwner().getId() == user.getId()) throw new NotFoundException("Владелец не может бронировать");
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
        if (booking.getStatus().equals(APPROVED)) throw new IllegalArgumentException("Бронирование уже подтверждено");
        if (userId == booking.getItem().getOwner().getId()) {
            BookingStatus status = (approved) ? APPROVED : REJECTED;
            booking.setStatus(status);
            return bookingRepository.save(booking);
        } else {
            throw new NotFoundException("Некорректный пользователь");
        }
    }

    @Override
    public Booking getBookingById(int userId, int bookingId) {
        userService.getUserById(userId).get();
        try {
            Booking booking = bookingRepository.findById(bookingId).get();
            if (userId == booking.getBooker().getId() || userId == booking.getItem().getOwner().getId()) {
                return booking;
            } else {
                throw new NotFoundException("Ошибка валидации запроса");
            }
        } catch (NotFoundException e) {
            throw new NotFoundException("Бронирование не найдено");
        }
    }

    @Override
    public List<Booking> getUserBookings(int userId, BookingState state) {
        userService.getUserById(userId).get();
        try {
            return switch (state) {
                case ALL -> bookingRepository.findByBooker_IdOrderByStartDesc(userId);
                case CURRENT -> bookingRepository.findByBooker_IdAndEndAfterAndStartBeforeOrderByStartDesc(userId,
                        LocalDateTime.now(), LocalDateTime.now());
                case PAST -> bookingRepository.findByBooker_IdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
                case FUTURE ->
                        bookingRepository.findByBooker_IdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
                case WAITING -> bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId, WAITING);
                case REJECTED -> bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId, REJECTED);
            };
        } catch (NotFoundException e) {
            throw new NotFoundException("Бронирования не найдены");
        }
    }

    @Override
    public List<Booking> getOwnerBookings(int ownerId, BookingState state) {
        userService.getUserById(ownerId).get();
        try {
            return switch (state) {
                case ALL -> bookingRepository.findByItem_OwnerIdOrderByStartDesc(ownerId);
                case CURRENT -> bookingRepository.findByItem_OwnerIdAndEndAfterAndStartBeforeOrderByStartDesc(ownerId,
                        LocalDateTime.now(), LocalDateTime.now());
                case PAST -> bookingRepository.findByItem_OwnerIdAndEndBeforeOrderByStartDesc(ownerId,
                        LocalDateTime.now());
                case FUTURE ->
                        bookingRepository.findByItem_OwnerIdAndStartAfterOrderByStartDesc(ownerId, LocalDateTime.now());
                case WAITING -> bookingRepository.findByItem_OwnerIdAndStatusOrderByStartDesc(ownerId, WAITING);
                case REJECTED -> bookingRepository.findByItem_OwnerIdAndStatusOrderByStartDesc(ownerId, REJECTED);
            };
        } catch (NotFoundException e) {
            throw new NotFoundException("Бронирования не найдены");
        }
    }

    @Override
    public List<Booking> getAllItemBookings(int itemId) {
        return bookingRepository.findByItem_IdOrderByStartDesc(itemId);
    }

    @Override
    public List<Booking> findByItem_IdAndBooker_IdAndStatusAndEndBefore(int itemId, int userId, BookingStatus status,
                                                                        LocalDateTime now) {
        return bookingRepository.findByItem_IdAndBooker_IdAndStatusAndEndBefore(itemId, userId, status, now);
    }
}