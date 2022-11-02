package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BookingDto {
    private int id;
    @Future
    private LocalDateTime start;
    @Future
    private LocalDateTime end;
    private Item item;
    private User booker;
    private BookingStatus status;
}
