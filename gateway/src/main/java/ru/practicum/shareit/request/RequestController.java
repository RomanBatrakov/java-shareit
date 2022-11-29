package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getAllRequests(@RequestHeader(HEADER_USER_ID) long userId) {
        log.info("Getting all requests: userId={}", userId);
        return requestClient.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader(HEADER_USER_ID) long userId,
                                             @PathVariable long requestId) {
        log.info("Getting request {}, userId={}", requestId, userId);
        return requestClient.getRequest(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllUsersRequests(@RequestHeader(HEADER_USER_ID) long userId,
                                                      @RequestParam(name = "from", required = false,
                                                              defaultValue = "0") @PositiveOrZero int from,
                                                      @RequestParam(name = "size", required = false,
                                                              defaultValue = "10") @Positive @Max(100) int size) {
        log.info("Getting all user requests: userId={}", userId);
        return requestClient.getAllUsersRequests(userId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader(HEADER_USER_ID) int userId,
                                                @RequestBody @Valid RequestDto requestDto) {
        log.info("Creating request {}, userId={}", requestDto, userId);
        return requestClient.createRequest(userId, requestDto);
    }
}