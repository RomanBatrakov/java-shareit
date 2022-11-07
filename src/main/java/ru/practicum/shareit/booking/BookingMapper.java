package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingToUserDto;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingDto toBookingDto(Booking booking);

    BookingToUserDto toBookingToUserDto(Booking booking);
}
