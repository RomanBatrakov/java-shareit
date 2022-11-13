package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {
    private final ItemRequestService service;

    @GetMapping
    public ResponseEntity<List<ItemRequestDto>> getAllRequests(@RequestHeader("X-Sharer-User-Id") int userId) {
        return ResponseEntity.ok(service.getAllRequests(userId));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestDto> getRequest(@RequestHeader("X-Sharer-User-Id") int userId,
                                                         @PathVariable int requestId) {
        return ResponseEntity.ok(service.getRequest(userId, requestId));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ItemRequestDto>> getAllUsersRequests(@RequestHeader("X-Sharer-User-Id") int userId,
                                                                    @Min(0) @RequestParam int from,
                                                                    @Min(1) @Max(100) @RequestParam int size) {
        return ResponseEntity.ok(service.getAllUsersRequests(userId, PageRequest.of(from, size)));
    }

    @PostMapping
    public ResponseEntity<ItemRequestDto> createRequest (@RequestHeader("X-Sharer-User-Id") int userId,
                                                         @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return ResponseEntity.ok(service.createRequest(userId, itemRequestDto));
    }
}