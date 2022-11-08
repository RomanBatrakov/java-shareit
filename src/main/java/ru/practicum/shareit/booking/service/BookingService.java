package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingSimpleDto;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    Booking createBooking(int userId, BookingSimpleDto bookingToUserDto);

    Booking updateBooking(int bookingId, int userId, Boolean approved);

    Booking getBookingById(int userId, int bookingId);

    List<Booking> getUserBookings(int userId, BookingState state);

    List<Booking> getOwnerBookings(int ownerId, BookingState state);

    List<Booking> getAllItemBookings(int itemId);

    List<Booking> findByItem_IdAndBooker_IdAndStatusAndEndBefore(int itemId, int userId, BookingStatus status,
                                                                 LocalDateTime now);
}