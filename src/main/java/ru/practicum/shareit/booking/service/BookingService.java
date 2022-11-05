package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingToUserDto;

public interface BookingService {
    Booking createBooking(int userId, BookingToUserDto bookingToUserDto);
}
