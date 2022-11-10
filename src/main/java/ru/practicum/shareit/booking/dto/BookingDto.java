package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
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
    private Item item;
    private User booker;
    private BookingStatus status;
}