package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.user.service.UserTestData.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRequestServiceImplTest {
    @Autowired
    private final ItemRequestServiceImpl itemRequestService;
    @Autowired
    private UserService userService;

    private ItemRequestDto itemRequest1;
    private ItemRequestDto itemRequest2;

    @BeforeEach
    void setUp() {
        itemRequest1 = ItemRequestDto.builder()
                .description("itemRequest1")
                .build();
        itemRequest2 = ItemRequestDto.builder()
                .description("itemRequest2")
                .build();
        userService.createUser(userDto1);
        itemRequestService.createRequest(1, itemRequest1);
    }

    @Test
    void getAllRequestsTest() {
        List<ItemRequestDto> itemRequestDtoFromSQL = itemRequestService.getAllRequests(1);
        assertThat(itemRequestDtoFromSQL.size(), equalTo(1));
    }

    @Test
    void getAllRequestsWrongIdTest() {
        assertThrows(NotFoundException.class, () -> itemRequestService.getAllRequests(10));
    }

    @Test
    void getRequestTest() {
        assertThat(itemRequestService.getRequest(1, 1).getDescription(),
                equalTo(itemRequest1.getDescription()));
    }

    @Test
    void getRequestWrongIdTest() {
        assertThrows(NotFoundException.class, () -> itemRequestService.getRequest(1, 2));
    }

    @Test
    void getAllUsersRequests() {
        userService.createUser(userDto2);
        itemRequestService.createRequest(2, itemRequest2);
        List<ItemRequestDto> itemRequestDtoFromSQL = itemRequestService.getAllUsersRequests(2,
                PageRequest.of(0, 10));
        assertThat(itemRequestDtoFromSQL.size(), equalTo(1));
    }

    @Test
    void createRequest() {
        itemRequestService.createRequest(1, itemRequest2);
        List<ItemRequestDto> itemRequestDtoFromSQL = itemRequestService.getAllRequests(1);
        assertThat(itemRequestDtoFromSQL.get(1).getDescription(), equalTo(itemRequest2.getDescription()));
        assertThat(itemRequestDtoFromSQL.get(1).getItems().size(), equalTo(0));
    }
}