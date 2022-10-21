package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Data
public class ItemRequest {
    private int id;
    @NotBlank
    @NotNull
    private String description;
    @NotNull
    private User requestor;
    @NotBlank
    @NotNull
    @PastOrPresent
    private LocalDateTime created;
}