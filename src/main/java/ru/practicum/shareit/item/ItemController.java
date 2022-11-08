package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService service;
    private final ItemMapper itemMapper;

    @GetMapping("/{itemId}")
    public ItemWithBookingsDto getItemById(@RequestHeader("X-Sharer-User-Id") int userId,
                                           @PathVariable int itemId) {
        return service.itemConverter(userId, service.getItemById(itemId).get());
    }

    @GetMapping
    public List<ItemWithBookingsDto> getAllOwnerItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        return service.getAllOwnerItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return service.search(text).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") int userId,
                              @Valid @RequestBody ItemDto itemDto) {
        return itemMapper.toItemDto(service.createItem(userId, itemMapper.toItem(itemDto)));
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@PathVariable int itemId,
                                    @RequestHeader("X-Sharer-User-Id") int userId,
                                    @Valid @RequestBody Comment comment) {
        return service.createComment(itemId, userId, comment);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable int itemId,
                              @RequestHeader("X-Sharer-User-Id") int userId,
                              @RequestBody ItemDto itemDto) {
        return itemMapper.toItemDto(service.updateItem(itemId, userId, itemMapper.toItem(itemDto)));
    }
}