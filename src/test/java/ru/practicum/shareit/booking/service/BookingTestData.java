package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingSimpleDto;

import java.time.LocalDateTime;

public class BookingTestData {
    public static final BookingSimpleDto bookingSimpleDto1 = BookingSimpleDto.builder()
            .start(LocalDateTime.now().plusDays(1))
            .end(LocalDateTime.now().plusDays(2))
            .itemId(1)
            .build();

    public static final BookingSimpleDto bookingSimpleDto2 = BookingSimpleDto.builder()
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusDays(1))
            .itemId(1)
            .build();

    public static final BookingSimpleDto createdDto = BookingSimpleDto.builder()
            .start(LocalDateTime.now().plusDays(5))
            .end(LocalDateTime.now().plusDays(6))
            .itemId(1)
            .build();
    public static final BookingSimpleDto createdWrongDto = BookingSimpleDto.builder()
            .start(LocalDateTime.now().plusDays(5))
            .end(LocalDateTime.now().plusDays(3))
            .itemId(1)
            .build();
}