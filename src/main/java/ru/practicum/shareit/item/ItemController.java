package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
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
    public ResponseEntity<List<ItemWithBookingsDto>> getAllOwnerItems(@RequestHeader("X-Sharer-User-Id") int userId,
                                                                      @PositiveOrZero @RequestParam(required = false,
                                                                              defaultValue = "0") int from,
                                                                      @Positive @RequestParam(required = false,
                                                                              defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getAllOwnerItems(userId, PageRequest.of((from / size), size)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> search(@RequestParam String text,
                                                @PositiveOrZero @RequestParam(required = false, defaultValue = "0") int from,
                                                @Positive @RequestParam(required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(service.search(text, PageRequest.of((from / size), size)));
    }

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestHeader("X-Sharer-User-Id") int userId,
                                              @Valid @RequestBody ItemDto itemDto) {
        return ResponseEntity.ok(service.createItem(userId, itemDto));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> createComment(@PathVariable int itemId,
                                                    @RequestHeader("X-Sharer-User-Id") int userId,
                                                    @Valid @RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(service.createComment(itemId, userId, commentDto));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable int itemId,
                                              @RequestHeader("X-Sharer-User-Id") int userId,
                                              @RequestBody ItemDto itemDto) {
        return ResponseEntity.ok(service.updateItem(itemId, userId, itemDto));
    }
}