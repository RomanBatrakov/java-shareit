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
    @NotBlank(message = "Description is mandatory")
    @NotNull(message = "Description is null")
    private String description;
    @NotNull(message = "Requestor is null")
    private User requestor;
    @NotBlank(message = "CreatedTime is mandatory")
    @NotNull(message = "CreatedTime is null")
    @PastOrPresent(message = "CreatedTime is future")
    private LocalDateTime created;
}
