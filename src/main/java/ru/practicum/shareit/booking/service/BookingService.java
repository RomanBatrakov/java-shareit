package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingToUserDto;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookingService {
    Booking createBooking(int userId, BookingToUserDto bookingToUserDto);

    Booking updateBooking(int bookingId, int userId, Boolean approved);

    Booking getBookingById(int userId, int bookingId);

    List<Booking> getUserBookings(int userId, BookingState state);
}
