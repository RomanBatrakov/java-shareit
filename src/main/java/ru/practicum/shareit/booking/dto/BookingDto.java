package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.lang.annotation.After;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BookingDto {
    private int id;
    @NotNull(message = "Date start is null")
    @FutureOrPresent(message = "Date start is past")
    private LocalDateTime start;
    @NotNull(message = "Date end is null")
    @FutureOrPresent(message = "Date end is past")
//    @DateTimeFormat(pattern="MM/dd/yyyy")
//    @Mapping(target="startDt", source="dto.employeeStartDt",
//            dateFormat="dd-MM-yyyy HH:mm:ss")
    private LocalDateTime end;
    @NotNull(message = "Item is null")
    private Item item;
    private User booker;
    private BookingStatus status;
}