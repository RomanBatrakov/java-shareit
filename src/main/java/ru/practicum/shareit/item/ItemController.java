package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService service;

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable int itemId) {
        return ItemMapper.toItemDto(service.getItemById(itemId));
    }

    @GetMapping
    public List<ItemDto> getAllOwnerItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        return service.getAllOwnerItems(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return service.search(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") int userId,
                              @Valid @RequestBody ItemDto itemDto) {
        return ItemMapper.toItemDto(service.createItem(userId, ItemMapper.toItem(itemDto)));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable int itemId,
                              @RequestHeader("X-Sharer-User-Id") int userId,
                              @RequestBody ItemDto itemDto) {
        return ItemMapper.toItemDto(service.updateItem(itemId, userId, ItemMapper.toItem(itemDto)));
    }
}
