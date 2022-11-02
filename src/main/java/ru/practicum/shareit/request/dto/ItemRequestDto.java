package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ItemRequestDto {
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
