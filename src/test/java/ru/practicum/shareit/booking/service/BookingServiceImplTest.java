package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.booking.service.BookingTestData.*;
import static ru.practicum.shareit.item.service.ItemTestData.itemDto1;
import static ru.practicum.shareit.user.service.UserTestData.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingServiceImplTest {
    @Autowired
    private final BookingService bookingService;
    @Autowired
    private final ItemService itemService;
    @Autowired
    private final UserService userService;
    @Autowired
    private final BookingRepository bookingRepository;
    @Autowired
    private final ItemMapper itemMapper;
    @Autowired
    private final UserMapper userMapper;
    UserDto bookerDto;
    ItemDto itemDto;

    @BeforeEach
    void setUp() {
        userService.createUser(userDto1);
        bookerDto = userService.createUser(userDto2);
        userService.createUser(userDto3);
        itemDto = itemService.createItem(1, itemDto1);
        bookingService.createBooking(2, bookingSimpleDto1);
    }

    @Test
    void createBookingTest() {
        bookingService.createBooking(2, createdDto);
        List<BookingDto> bookings = bookingService.getUserBookings(
                2, "WAITING", PageRequest.of(0, 10));
        assertThat(bookings.size(), equalTo(2));
    }

    @Test
    void createBookingWrongDateTest() {
        RuntimeException ex = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.createBooking(2, createdWrongDto));
        assertEquals("Ошибка входящих данных", ex.getMessage());
    }

    @Test
    void updateBookingTest() {
        bookingService.updateBooking(1, 1, false);
        List<BookingDto> userBookings = bookingService.getUserBookings(
                2, "REJECTED", PageRequest.of(0, 10));
        assertThat(userBookings.size(), equalTo(1));
        List<BookingDto> ownerBookings = bookingService.getOwnerBookings(
                1, "REJECTED", PageRequest.of(0, 10));
        assertThat(ownerBookings.size(), equalTo(1));
    }

    @Test
    void updateBookingWrongUserTest() {
        assertThrows(NotFoundException.class, () -> bookingService.updateBooking(1, 2, false));
    }

    @Test
    void updateBookingAlreadyApprovedTest() {
        bookingService.updateBooking(1, 1, true);
        assertThrows(IllegalArgumentException.class, () -> bookingService.updateBooking(
                1, 1, true));
    }

    @Test
    void getBookingById() {
        bookingService.createBooking(2, createdDto);
        List<BookingDto> bookings = bookingService.getUserBookings(
                2, "ALL", PageRequest.of(0, 10));
        assertThat(bookings.size(), equalTo(2));
    }

    @Test
    void getBookingByWrongBookingIdTest() {
        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(2, 10));
    }

    @Test
    void getBookingByWrongUserIdTest() {
        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(3, 1));
    }

    @Test
    void getUserBookingsTest() {
        bookingService.createBooking(2, createdDto);
        List<BookingDto> bookings = bookingService.getUserBookings(
                2, "ALL", PageRequest.of(0, 10));
        assertThat(bookings.get(0).getEnd().toString(), equalTo(createdDto.getEnd().toString()));
    }

    @Test
    void getWrongStatusUserBookingsTest() {
        String state = "WRONG";
        RuntimeException ex = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.getUserBookings(2, state, PageRequest.of(0, 10)));
        assertEquals("Unknown state: " + state, ex.getMessage());
    }

    @Test
    void getDiffStatusUserBookingsTest() {
        bookingService.createBooking(2, createdDto);
        List<BookingDto> waitingBookings = bookingService.getUserBookings(
                2, "WAITING", PageRequest.of(0, 10));
        assertThat(waitingBookings.size(), equalTo(2));

        bookingService.updateBooking(2, 1, true);
        List<BookingDto> futureBookings = bookingService.getUserBookings(
                2, "FUTURE", PageRequest.of(0, 10));
        assertThat(futureBookings.size(), equalTo(2));

        bookingService.createBooking(2, bookingSimpleDto2);
        bookingService.updateBooking(3, 1, true);
        List<BookingDto> currentBookings = bookingService.getUserBookings(
                2, "CURRENT", PageRequest.of(0, 10));
        assertThat(currentBookings.size(), equalTo(1));
    }

    @Test
    void getPastStatusUserAndOwnerBookingsTest() {
        Booking booking = Booking.builder()
                .start(LocalDateTime.of(2022, 8, 1, 12, 15, 1))
                .end(LocalDateTime.of(2022, 8, 2, 12, 15, 1))
                .item(itemMapper.toItem(itemDto))
                .booker(userMapper.toUser(bookerDto))
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking);
        List<BookingDto> pastUserBookings = bookingService.getUserBookings(
                2, "PAST", PageRequest.of(0, 10));
        assertThat(pastUserBookings.size(), equalTo(1));
        List<BookingDto> pastOwnerBookings = bookingService.getOwnerBookings(
                1, "PAST", PageRequest.of(0, 10));
        assertThat(pastOwnerBookings.size(), equalTo(1));
    }

    @Test
    void getOwnerBookingsTest() {
        List<BookingDto> bookings = bookingService.getOwnerBookings(
                1, "ALL", PageRequest.of(0, 10));
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    void getWrongStatusOwnerBookingsTest() {
        String state = "WRONG";
        RuntimeException ex = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.getOwnerBookings(2, state, PageRequest.of(0, 10)));
        assertEquals("Unknown state: " + state, ex.getMessage());
    }

    @Test
    void getDiffStatusOwnerBookingsTest() {
        bookingService.createBooking(2, createdDto);
        List<BookingDto> waitingBookings = bookingService.getOwnerBookings(
                1, "WAITING", PageRequest.of(0, 10));
        assertThat(waitingBookings.size(), equalTo(2));

        bookingService.updateBooking(2, 1, true);
        List<BookingDto> futureBookings = bookingService.getOwnerBookings(
                1, "FUTURE", PageRequest.of(0, 10));
        assertThat(futureBookings.size(), equalTo(2));

        bookingService.createBooking(2, bookingSimpleDto2);
        bookingService.updateBooking(3, 1, true);
        List<BookingDto> currentBookings = bookingService.getOwnerBookings(
                1, "CURRENT", PageRequest.of(0, 10));
        assertThat(currentBookings.size(), equalTo(1));
    }
}