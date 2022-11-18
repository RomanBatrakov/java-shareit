package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig({ItemRequestController.class, WebConfig.class})
class ItemRequestControllerTestWithContext {

    private final ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();

    private final ItemRequestService itemRequestService;
    private MockMvc mvc;
    private ItemRequestDto itemRequestDto;

    @Autowired
    ItemRequestControllerTestWithContext(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @BeforeEach
    void setUp(WebApplicationContext wac) {
        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
        UserDto userDto = new UserDto(1, "John", "john.doe@mail.com");
        itemRequestDto = new ItemRequestDto(
                1,
                "description",
                userDto,
                LocalDateTime.now().withNano(0),
                new ArrayList<>());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void getAllRequests() throws Exception {
        when(itemRequestService.getAllRequests(anyInt()))
                .thenReturn(List.of(itemRequestDto));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemRequestDto.getId())))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$[0].requestor.name", is("John")))
                .andExpect(jsonPath("$[0].created", is(itemRequestDto.getCreated().withNano(0)
                        .toString())))
                .andExpect(jsonPath("$[0].items", is(itemRequestDto.getItems())));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void getRequest() throws Exception {
        when(itemRequestService.getRequest(anyInt(), anyInt()))
                .thenReturn(itemRequestDto);

        mvc.perform(get("/requests/{requestId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId())))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.requestor.name", is("John")))
                .andExpect(jsonPath("$.created", is(itemRequestDto.getCreated().withNano(0)
                        .toString())))
                .andExpect(jsonPath("$.items", is(itemRequestDto.getItems())));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void getAllUsersRequests() throws Exception {
        when(itemRequestService.getAllUsersRequests(anyInt(), any()))
                .thenReturn(List.of(itemRequestDto));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemRequestDto.getId())))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$[0].requestor.name", is("John")))
                .andExpect(jsonPath("$[0].created", is(itemRequestDto.getCreated().withNano(0)
                        .toString())))
                .andExpect(jsonPath("$[0].items", is(itemRequestDto.getItems())));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void createRequest() throws Exception {
        itemRequestDto = ItemRequestDto.builder()
                .id(1)
                .description("description")
                .build();
        when(itemRequestService.createRequest(anyInt(), any()))
                .thenReturn(itemRequestDto);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId())))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())));
    }
}