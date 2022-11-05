package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingToUserDto;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

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
        if (bookingToUserDto.getStart().isBefore(bookingToUserDto.getEnd())) {
            Booking booking = Booking.builder()
                    .start(bookingToUserDto.getStart())
                    .end(bookingToUserDto.getEnd())
                    .item(item)
                    .booker(user)
                    .status(BookingStatus.WAITING)
                    .build();
            return bookingRepository.save(booking);
        } else {
            throw new NotFoundException("Ошибка входящих данных");
        }
    }
}