package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemWithBookingsById(@RequestHeader(HEADER_USER_ID) long userId,
                                                          @PathVariable long itemId) {
        log.info("Getting item {}, userId={}", itemId, userId);
        return itemClient.getItemWithBookingsById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllOwnerItems(@RequestHeader(HEADER_USER_ID) long userId,
                                                   @RequestParam(name = "from", required = false,
                                                           defaultValue = "0") @PositiveOrZero int from,
                                                   @RequestParam(name = "size", required = false,
                                                           defaultValue = "10") @Positive @Max(100) int size) {
        log.info("Getting all owner items userId={}, from={}, size={}", userId, from, size);
        return itemClient.getAllOwnerItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text,
                                         @RequestParam(name = "from", required = false,
                                                 defaultValue = "0") @PositiveOrZero int from,
                                         @RequestParam(name = "size", required = false,
                                                 defaultValue = "10") @Positive @Max(100) int size) {
        log.info("Searching text={}, from={}, size={}", text, from, size);
        return itemClient.search(text, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(HEADER_USER_ID) long userId,
                                             @RequestBody @Valid ItemDto itemDto) {
        log.info("Creating item {}, userId={}", itemDto, userId);
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(HEADER_USER_ID) long userId,
                                             @PathVariable long itemId,
                                             @RequestBody ItemDto itemDto) {
        log.info("Updating item {}, itemId={}, userId={}", itemDto, itemId, userId);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(HEADER_USER_ID) long userId,
                                                @PathVariable long itemId,
                                                @RequestBody @Valid CommentDto commentDto) {
        log.info("Creating comment {}, userId={}, itemId={}", commentDto, userId, itemId);
        return itemClient.createComment(itemId, userId, commentDto);
    }
}
