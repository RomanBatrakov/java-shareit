package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private long id;
    @NotNull(message = "Name is null")
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotNull(message = "Description is null")
    @NotBlank(message = "Description is mandatory")
    private String description;
    @NotNull(message = "Available is null")
    private Boolean available;
    private Integer requestId;
}