package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.item.service.ItemTestData.itemDto1;
import static ru.practicum.shareit.user.service.UserTestData.userDto1;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemServiceImplTest {
    @Autowired
    private final ItemServiceImpl itemService;
    @Autowired
    private final UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService.createUser(userDto1);
        itemService.createItem(1, itemDto1);
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
        List<ItemWithBookingsDto> itemRequestDtoFromSQL = itemService.getAllOwnerItems(1, PageRequest.of(0, 10));
        assertThat(itemRequestDtoFromSQL.size(), equalTo(1));
    }

    @Test
    void search() {
    }

    @Test
    void createItem() {
    }

    @Test
    void updateItem() {
    }

    @Test
    void itemConverter() {
    }

    @Test
    void createComment() {
    }

    @Test
    void getRequestItems() {
    }

    @Test
    void getItemWithBookingsById() {
    }
}