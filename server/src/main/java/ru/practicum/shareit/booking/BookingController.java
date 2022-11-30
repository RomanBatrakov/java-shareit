package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSimpleDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingService service;

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestHeader(HEADER_USER_ID) Long userId,
                                                    @RequestBody BookingSimpleDto bookingSimpleDto) {
        return ResponseEntity.ok(service.createBooking(userId, bookingSimpleDto));
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> updateBooking(@PathVariable Long bookingId,
                                                    @RequestParam Boolean approved,
                                                    @RequestHeader(HEADER_USER_ID) Long userId) {
        return ResponseEntity.ok(service.updateBooking(bookingId, userId, approved));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getBookingById(@RequestHeader(HEADER_USER_ID) Long userId,
                                                     @PathVariable Long bookingId) {
        return ResponseEntity.ok(service.getBookingById(userId, bookingId));
    }

    @GetMapping
    public ResponseEntity<List<BookingDto>> getUserBookings(@RequestHeader(HEADER_USER_ID) Long userId,
                                                            @RequestParam(required = false,
                                                                    defaultValue = "ALL") String state,
                                                            @RequestParam(required = false,
                                                                    defaultValue = "0") Integer from,
                                                            @RequestParam(required = false,
                                                                    defaultValue = "10") Integer size) {
        return ResponseEntity.ok(service.getUserBookings(userId, state, PageRequest.of((from / size), size)));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDto>> getOwnerBookings(@RequestHeader(HEADER_USER_ID) Long ownerId,
                                                             @RequestParam(required = false,
                                                                     defaultValue = "ALL") String state,
                                                             @RequestParam(required = false,
                                                                     defaultValue = "0") Integer from,
                                                             @RequestParam(required = false,
                                                                     defaultValue = "10") Integer size) {
        return ResponseEntity.ok(service.getOwnerBookings(ownerId, state, PageRequest.of((from / size), size)));
    }
}