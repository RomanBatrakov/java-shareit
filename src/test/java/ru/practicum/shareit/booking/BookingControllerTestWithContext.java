package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSimpleDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.config.WebConfig;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitWebConfig({BookingController.class, WebConfig.class})
class BookingControllerTestWithContext {
    private final ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
    private final BookingService bookingService;
    private MockMvc mvc;
    private BookingDto bookingDto;
    private BookingSimpleDto bookingSimpleDto;

    @Autowired
    BookingControllerTestWithContext(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @BeforeEach
    void setUp(WebApplicationContext wac) {
        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
        UserDto booker = new UserDto(1, "John", "john.doe@mail.com");
        UserDto owner = new UserDto(2, "John2", "john2.doe@mail.com");
        ItemDto item = new ItemDto(1, "John", "description", true, owner, 1);
        bookingDto = new BookingDto(
                1,
                LocalDateTime.now().withNano(0),
                LocalDateTime.MAX.withNano(0),
                item,
                booker,
                BookingStatus.WAITING);
        bookingSimpleDto = BookingSimpleDto.builder()
                .id(1)
                .start(LocalDateTime.now().withNano(0))
                .end(LocalDateTime.MAX.withNano(0))
                .itemId(1)
                .bookerId(1)
                .build();
    }

    @Test
    void createBooking() {
    }

    @Test
    void updateBooking() {
    }

    @Test
    void getBookingById() {
    }

    @Test
    void getUserBookings() {
    }

    @Test
    void getOwnerBookings() {
    }
}