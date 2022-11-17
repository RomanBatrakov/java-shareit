package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSimpleDto;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    BookingDto createBooking(int userId, BookingSimpleDto bookingSimpleDto);

    BookingDto updateBooking(int bookingId, int userId, Boolean approved);

    BookingDto getBookingById(int userId, int bookingId);

    List<BookingDto> getUserBookings(int userId, String state, Pageable pageable);

    List<BookingDto> getOwnerBookings(int ownerId, String state, Pageable pageable);

    List<Booking> getAllItemBookings(int itemId);

    List<Booking> findByItem_IdAndBooker_IdAndStatusAndEndBefore(int itemId, int userId, BookingStatus status,
                                                                 LocalDateTime now);
}