package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSimpleDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.booking.BookingStatus.WAITING;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @Autowired
    ObjectMapper mapper;
    @MockBean
    BookingService bookingService;
    @Autowired
    private MockMvc mvc;
    private BookingDto bookingDto;
    private BookingSimpleDto bookingSimpleDto;

    @BeforeEach
    void setUp() {
        UserDto booker = new UserDto(1L, "John", "john.doe@mail.com");
        UserDto owner = new UserDto(2L, "John2", "john2.doe@mail.com");
        ItemDto item = new ItemDto(1L, "Item", "description", true, owner, 1L);
        bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2022, 11, 30, 1, 1, 1))
                .end(LocalDateTime.of(2022, 12, 30, 1, 1, 1))
                .item(item)
                .booker(booker)
                .status(WAITING)
                .build();
        bookingSimpleDto = BookingSimpleDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2022, 11, 30, 1, 1, 1))
                .end(LocalDateTime.of(2022, 12, 30, 1, 1, 1))
                .itemId(1L)
                .build();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void createBooking() throws Exception {
        when(bookingService.createBooking(anyLong(), any()))
                .thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingSimpleDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) bookingDto.getId())))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
        verify(bookingService).createBooking(anyLong(), any());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void updateBooking() throws Exception {
        when(bookingService.updateBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDto);

        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .param("approved", String.valueOf(true))
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) bookingDto.getId())))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
        verify(bookingService).updateBooking(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void getBookingById() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) bookingDto.getId())))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
        verify(bookingService).getBookingById(anyLong(), anyLong());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void getUserBookings() throws Exception {
        when(bookingService.getUserBookings(anyLong(), anyString(), any()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is((int) bookingDto.getId())))
                .andExpect(jsonPath("$[0].start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$[0].end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$[0].item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$[0].booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().toString())));
        verify(bookingService).getUserBookings(anyLong(), anyString(), any());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void getOwnerBookings() throws Exception {
        when(bookingService.getOwnerBookings(anyLong(), anyString(), any()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is((int) bookingDto.getId())))
                .andExpect(jsonPath("$[0].start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$[0].end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$[0].item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$[0].booker.name", is(bookingDto.getBooker().getName())))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().toString())));
        verify(bookingService).getOwnerBookings(anyLong(), anyString(), any());
    }

    @Test
    void getOwnerBookingsError() throws Exception {
        when(bookingService.getOwnerBookings(anyLong(), eq("ALL"), any()))
                .thenThrow(IllegalArgumentException.class);

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isBadRequest());
    }
}