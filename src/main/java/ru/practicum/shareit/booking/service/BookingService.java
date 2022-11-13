package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSimpleDto;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    BookingDto createBooking(int userId, BookingSimpleDto bookingToUserDto);

    BookingDto updateBooking(int bookingId, int userId, Boolean approved);

    BookingDto getBookingById(int userId, int bookingId);

    List<BookingDto> getUserBookings(int userId, String state);

    List<BookingDto> getOwnerBookings(int ownerId, String state);

    List<Booking> getAllItemBookings(int itemId);

    List<Booking> findByItem_IdAndBooker_IdAndStatusAndEndBefore(int itemId, int userId, BookingStatus status,
                                                                 LocalDateTime now);
}