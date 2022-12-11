package ru.practicum.shareit.request.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    private long id;
    @NotNull(message = "Description is null")
    @NotBlank(message = "Description is mandatory")
    private String description;
}