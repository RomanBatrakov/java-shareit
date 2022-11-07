package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingToUserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ItemWithBookingsDto {
    private int id;
    @NotNull(message = "Name is null")
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotNull(message = "Description is null")
    @NotBlank(message = "Description is mandatory")
    private String description;
    @NotNull(message = "Available is null")
    private Boolean available;
    private BookingToUserDto lastBooking;
    private BookingToUserDto nextBooking;
//    private User owner;
//    private ItemRequest request;
}
