package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSimpleDto;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    BookingDto createBooking(long userId, BookingSimpleDto bookingSimpleDto);

    BookingDto updateBooking(long bookingId, long userId, Boolean approved);

    BookingDto getBookingById(long userId, long bookingId);

    List<BookingDto> getUserBookings(long userId, String state, Pageable pageable);

    List<BookingDto> getOwnerBookings(long ownerId, String state, Pageable pageable);

    List<Booking> getAllItemBookings(long itemId);

    List<Booking> findByItem_IdAndBooker_IdAndStatusAndEndBefore(
            long itemId, long userId, BookingStatus status, LocalDateTime now);
}