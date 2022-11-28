package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(HEADER_USER_ID) long userId,
                                                @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.createBooking(userId, requestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(@RequestHeader(HEADER_USER_ID) long userId,
                                                @PathVariable long bookingId,
                                                @RequestParam Boolean approved) {
        log.info("Updating booking {}, userId={}, approved={}", bookingId, userId, approved);
        return bookingClient.updateBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(HEADER_USER_ID) long userId,
                                                 @PathVariable long bookingId) {
        log.info("Getting booking {}, userId={}", bookingId, userId);
        return bookingClient.getBookingById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserBookings(@RequestHeader(HEADER_USER_ID) long userId,
                                                  @RequestParam(name = "state", required = false,
                                                          defaultValue = "ALL") String stateParam,
                                                  @RequestParam(name = "from", required = false,
                                                          defaultValue = "0") @PositiveOrZero int from,
                                                  @RequestParam(name = "size", required = false,
                                                          defaultValue = "10") @Positive @Max(100) int size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Getting user bookings with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getUserBookings(userId, state, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader(HEADER_USER_ID) long ownerId,
                                                   @RequestParam(name = "state", required = false,
                                                           defaultValue = "ALL") String stateParam,
                                                   @RequestParam(name = "from", required = false,
                                                           defaultValue = "0") @PositiveOrZero int from,
                                                   @RequestParam(name = "size", required = false,
                                                           defaultValue = "10") @Positive @Max(100) int size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Getting owner bookings with state {}, ownerId={}, from={}, size={}", stateParam, ownerId, from, size);
        return bookingClient.getOwnerBookings(ownerId, state, from, size);
    }
}