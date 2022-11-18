package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private int id;
    @NotNull(message = "Date start is null")
    @FutureOrPresent(message = "Date start is past")
    private LocalDateTime start;
    @NotNull(message = "Date end is null")
    @FutureOrPresent(message = "Date end is past")
    private LocalDateTime end;
    @NotNull(message = "Item is null")
    private ItemDto item;
    private UserDto booker;
    private BookingStatus status;
}