package ru.practicum.shareit.item;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getOwner(),
                item.getRequest() != null ? item.getRequest() : null
        );
    }
}
