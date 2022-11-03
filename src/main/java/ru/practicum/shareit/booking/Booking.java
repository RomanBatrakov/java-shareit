package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Booking {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private User booker;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
