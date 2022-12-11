package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService service;
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";


    @GetMapping("/{itemId}")
    public ResponseEntity<ItemWithBookingsDto> getItemWithBookingsById(@RequestHeader(HEADER_USER_ID) Long userId,
                                                                       @PathVariable Long itemId) {
        return ResponseEntity.ok(service.getItemWithBookingsById(userId, itemId));
    }

    @GetMapping
    public ResponseEntity<List<ItemWithBookingsDto>> getAllOwnerItems(@RequestHeader(HEADER_USER_ID) Long userId,
                                                                      @RequestParam(required = false,
                                                                              defaultValue = "0") Integer from,
                                                                      @RequestParam(required = false,
                                                                              defaultValue = "10") Integer size) {
        return ResponseEntity.ok(service.getAllOwnerItems(userId, PageRequest.of((from / size), size)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> search(@RequestParam String text,
                                                @RequestParam(required = false, defaultValue = "0") Integer from,
                                                @RequestParam(required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok(service.search(text, PageRequest.of((from / size), size)));
    }

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestHeader(HEADER_USER_ID) Long userId,
                                              @RequestBody ItemDto itemDto) {
        return ResponseEntity.ok(service.createItem(userId, itemDto));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> createComment(@PathVariable Long itemId,
                                                    @RequestHeader(HEADER_USER_ID) Long userId,
                                                    @RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(service.createComment(itemId, userId, commentDto));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable Long itemId,
                                              @RequestHeader(HEADER_USER_ID) Long userId,
                                              @RequestBody ItemDto itemDto) {
        return ResponseEntity.ok(service.updateItem(itemId, userId, itemDto));
    }
}