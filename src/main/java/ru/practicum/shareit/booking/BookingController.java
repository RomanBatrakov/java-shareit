package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSimpleDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingService service;

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                                    @Valid @RequestBody BookingSimpleDto bookingToUserDto) {
        return ResponseEntity.ok(service.createBooking(userId, bookingToUserDto));
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> updateBooking(@PathVariable int bookingId,
                                                    @RequestParam Boolean approved,
                                                    @RequestHeader("X-Sharer-User-Id") int userId) {
        return ResponseEntity.ok(service.updateBooking(bookingId, userId, approved));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getBookingById(@RequestHeader("X-Sharer-User-Id") int userId,
                                                     @PathVariable int bookingId) {
        return ResponseEntity.ok(service.getBookingById(userId, bookingId));
    }

    @GetMapping
    public ResponseEntity<List<BookingDto>> getUserBookings(@RequestHeader("X-Sharer-User-Id") int userId,
                                                            @RequestParam(required = false,
                                                                    defaultValue = "ALL") String state) {
        return ResponseEntity.ok(service.getUserBookings(userId, state));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDto>> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                                             @RequestParam(required = false,
                                                                     defaultValue = "ALL") String state) {
        return ResponseEntity.ok(service.getOwnerBookings(ownerId, state));
    }
}