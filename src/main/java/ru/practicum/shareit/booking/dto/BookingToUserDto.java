package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
public class BookingToUserDto {
    private int id;
    @NotNull
    @FutureOrPresent
    private LocalDateTime start;
    @NotNull
    @FutureOrPresent
//    @DateTimeFormat(pattern="MM/dd/yyyy")
//    @Mapping(target="startDt", source="dto.employeeStartDt",
//            dateFormat="dd-MM-yyyy HH:mm:ss")
    private LocalDateTime end;
    @NotNull
    private int itemId;
    private User booker;
    private BookingStatus status;
}
