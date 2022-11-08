package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingSimpleDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

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
    private BookingSimpleDto lastBooking;
    private BookingSimpleDto nextBooking;
    private List<CommentDto> comments;
    //    TODO: реализовать систему запросов
    //    private ItemRequest request;
}