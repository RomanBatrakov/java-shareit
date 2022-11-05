package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingToUserDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingService service;
    private final BookingMapper bookingMapper;
    @PostMapping
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                 @Valid @RequestBody BookingToUserDto bookingToUserDto) {
        return bookingMapper.toBookingDto(service.createBooking(userId, bookingToUserDto));
    }
}
