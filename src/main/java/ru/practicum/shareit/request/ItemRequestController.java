package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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
    public ResponseEntity<List<ItemRequestDto>> getAllUsersRequests(@RequestHeader("X-Sharer-User-Id") int userId,
                                                                    @PositiveOrZero @RequestParam(required = false,
                                                                            defaultValue = "0") int from,
                                                                    @Positive @RequestParam(required = false,
                                                                            defaultValue = "10")
                                                                    int size) {
        return ResponseEntity.ok(service.getAllUsersRequests(userId, PageRequest.of((from / size), size)));
    }

    @PostMapping
    public ResponseEntity<ItemRequestDto> createRequest(@RequestHeader("X-Sharer-User-Id") int userId,
                                                        @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return ResponseEntity.ok(service.createRequest(userId, itemRequestDto));
    }
}