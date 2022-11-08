package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSimpleDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingService service;
    private final BookingMapper bookingMapper;

    @PostMapping
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                    @Valid @RequestBody BookingSimpleDto bookingToUserDto) {
        return bookingMapper.toBookingDto(service.createBooking(userId, bookingToUserDto));
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBooking(@PathVariable int bookingId,
                                    @RequestParam Boolean approved,
                                    @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingMapper.toBookingDto(service.updateBooking(bookingId, userId, approved));
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") int userId,
                                     @PathVariable int bookingId) {
        return bookingMapper.toBookingDto(service.getBookingById(userId, bookingId));
    }

    @GetMapping
    public List<BookingDto> getUserBookings(@RequestHeader("X-Sharer-User-Id") int userId,
                                            @RequestParam(required = false, defaultValue = "ALL") String state) {
        try {
            BookingState bookingState = BookingState.valueOf(state);
            return service.getUserBookings(userId, bookingState).stream()
                    .map(bookingMapper::toBookingDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                       @RequestParam(required = false, defaultValue = "ALL") String state) {
        try {
            BookingState bookingState = BookingState.valueOf(state);
        return service.getOwnerBookings(ownerId, bookingState).stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
    }
}