package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService service;

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemWithBookingsDto> getItemById(@RequestHeader("X-Sharer-User-Id") int userId,
                                                           @PathVariable int itemId) {
        return ResponseEntity.ok(service.itemConverter(userId, service.getItemById(itemId).get()));
    }

    @GetMapping
    public ResponseEntity<List<ItemWithBookingsDto>> getAllOwnerItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        return ResponseEntity.ok(service.getAllOwnerItems(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> search(@RequestParam String text) {
        return ResponseEntity.ok(service.search(text));
    }

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestHeader("X-Sharer-User-Id") int userId,
                                              @Valid @RequestBody ItemDto itemDto) {
        return ResponseEntity.ok(service.createItem(userId, itemDto));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> createComment(@PathVariable int itemId,
                                                    @RequestHeader("X-Sharer-User-Id") int userId,
                                                    @Valid @RequestBody Comment comment) {
        return ResponseEntity.ok(service.createComment(itemId, userId, comment));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable int itemId,
                                              @RequestHeader("X-Sharer-User-Id") int userId,
                                              @Valid @RequestBody ItemDto itemDto) {
        return ResponseEntity.ok(service.updateItem(itemId, userId, itemDto));
    }
}