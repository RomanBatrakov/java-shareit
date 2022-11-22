package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
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
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.item.service.ItemTestData.*;
import static ru.practicum.shareit.user.service.UserTestData.userDto1;
import static ru.practicum.shareit.user.service.UserTestData.userDto2;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemServiceImplTest {
    @Autowired
    private final ItemServiceImpl itemService;
    @Autowired
    private final UserServiceImpl userService;

    @Autowired
    private final BookingRepository bookingRepository;

    @Autowired
    private final ItemRequestServiceImpl itemRequestService;

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
        itemDto = itemService.createItem(1, itemDto1);
    }

    @Test
    void getItemByIdTest() {
        Item itemFromSQL = itemService.getItemById(1).get();
        assertThat(itemFromSQL.getName(), equalTo(itemDto1.getName()));
    }

    @Test
    void getItemByWrongIdTest() {
        assertThrows(NotFoundException.class, () -> itemService.getItemById(100));
    }

    @Test
    void getAllOwnerItemsTest() {
        List<ItemWithBookingsDto> items = itemService.getAllOwnerItems(1, PageRequest.of(0, 10));
        assertThat(items.size(), equalTo(1));
    }

    @Test
    void searchTest() {
        List<ItemDto> items = itemService.search("description1", PageRequest.of(0, 10));
        assertThat(items.size(), equalTo(1));
        items = itemService.search("description2", PageRequest.of(0, 10));
        assertThat(items.size(), equalTo(0));
    }

    @Test
    void createItemTest() {
        itemService.createItem(1, itemDtoCreated);
        List<ItemWithBookingsDto> items = itemService.getAllOwnerItems(1, PageRequest.of(0, 10));
        assertThat(items.get(1).getName(), equalTo(itemDtoCreated.getName()));
    }

    @Test
    void createItemWithIdTest() {
        assertThrows(NotFoundException.class, () -> itemService.createItem(1, itemDto4));
    }

    @Test
    void updateItemTest() {
        itemDto1.setName("item1update");
        itemService.updateItem(1, 1, itemDto1);
        List<ItemWithBookingsDto> items = itemService.getAllOwnerItems(1, PageRequest.of(0, 10));
        assertThat(items.get(0).getName(), equalTo("item1update"));
    }

    @Test
    void updateItemWrongUserIdTest() {
        assertThrows(NotFoundException.class, () -> itemService.updateItem(1, 2, itemDto1));
    }

    @Test
    void createCommentTest() {
        Booking booking = Booking.builder()
                .start(LocalDateTime.of(2022, 8, 1, 12, 15, 1))
                .end(LocalDateTime.of(2022, 8, 2, 12, 15, 1))
                .item(itemMapper.toItem(itemDto))
                .booker(userMapper.toUser(bookerDto))
                .status(BookingStatus.APPROVED)
                .build();
        bookingRepository.save(booking);
        itemService.createComment(1, 2, commentDto);
        List<ItemWithBookingsDto> items = itemService.getAllOwnerItems(1, PageRequest.of(0, 10));
        assertThat(items.get(0).getComments().get(0).getText(), equalTo(commentDto.getText()));
    }

    @Test
    void createCommentFailTest() {
        assertThrows(IllegalArgumentException.class, () -> itemService.createComment(1, 1, commentDto));

    }

    @Test
    void getRequestItemsTest() {
        ItemRequestDto itemRequest1 = ItemRequestDto.builder()
                .description("itemRequest1")
                .build();
        itemRequestService.createRequest(2, itemRequest1);
        itemService.createItem(1, itemDto2);
        List<ItemDto> items = itemService.getRequestItems(1);
        assertThat(items.size(), equalTo(1));
    }

    @Test
    void getItemWithBookingsById() {
        ItemWithBookingsDto item = itemService.getItemWithBookingsById(1, 1);
        assertThat(item.getNextBooking(), equalTo(null));
        assertThat(item.getLastBooking(), equalTo(null));
        assertThat(item.getName(), equalTo(itemDto1.getName()));
    }
}