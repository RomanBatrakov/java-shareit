package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.shareit.config.WebConfig;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig({ItemController.class, WebConfig.class})
class ItemControllerTestWithContext {

    private final ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
    private final ItemService itemService;
    private MockMvc mvc;
    private ItemDto itemDto;
    private ItemWithBookingsDto itemWithBookingsDto;

    @Autowired
    ItemControllerTestWithContext(ItemService itemService) {
        this.itemService = itemService;
    }

    @BeforeEach
    void setUp(WebApplicationContext wac) {
        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
        UserDto owner = new UserDto(1, "John", "john.doe@mail.com");
        itemDto = new ItemDto(
                1,
                "name",
                "description",
                true,
                owner,
                1);
        itemWithBookingsDto = ItemWithBookingsDto.builder()
                .id(1)
                .name("name")
                .description("description")
                .available(true)
                .build();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void getItemById() throws Exception {
        when(itemService.getItemWithBookingsById(anyInt(), anyInt()))
                .thenReturn(itemWithBookingsDto);

        mvc.perform(get("/items/{itemId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemWithBookingsDto.getId())))
                .andExpect(jsonPath("$.description", is(itemWithBookingsDto.getDescription())))
                .andExpect(jsonPath("$.name", is(itemWithBookingsDto.getName())))
                .andExpect(jsonPath("$.available", is(itemWithBookingsDto.getAvailable())));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void getAllOwnerItems() throws Exception {
        when(itemService.getAllOwnerItems(anyInt(), any()))
                .thenReturn(List.of(itemWithBookingsDto));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemWithBookingsDto.getId())))
                .andExpect(jsonPath("$[0].description", is(itemWithBookingsDto.getDescription())))
                .andExpect(jsonPath("$[0].name", is(itemWithBookingsDto.getName())))
                .andExpect(jsonPath("$[0].available", is(itemWithBookingsDto.getAvailable())));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void search() throws Exception {
        when(itemService.search(anyString(), any()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search?text='name'")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemDto))));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void createItem() throws Exception {
        when(itemService.createItem(anyInt(), any()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.owner.name", is(itemDto.getOwner().getName())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId())));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void createComment() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .text("text")
                .authorName("name")
                .created(LocalDateTime.now().withNano(0))
                .build();
        when(itemService.createComment(anyInt(), anyInt(), any()))
                .thenReturn(commentDto);

        mvc.perform(post("/items/{itemId}/comment", 1)
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId())))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())))
                .andExpect(jsonPath("$.created", is(commentDto.getCreated().withNano(0)
                        .toString())));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void updateItem() throws Exception {
        when(itemService.updateItem(anyInt(), anyInt(), any()))
                .thenReturn(itemDto);

        mvc.perform(patch("/items/{itemId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.owner.name", is(itemDto.getOwner().getName())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId())));
    }
}