package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    private int id;
    @NotNull(message = "Description is null")
    @NotBlank(message = "Description is mandatory")
    private String description;
    private User requestor;
    @FutureOrPresent(message = "Date is past")
    private LocalDateTime created;
    private List<ItemDto> items;
}