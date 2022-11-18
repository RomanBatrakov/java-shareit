package ru.practicum.shareit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;

import static org.mockito.Mockito.mock;

@Configuration // помечает класс как java-config для контекста приложения
@EnableWebMvc  // призывает импортировать дополнительную конфигурацию для веб-приложений
public class WebConfig {
    @Bean
    public UserService userService() {
        return mock(UserService.class);
    }

    @Bean
    public ItemRequestService itemRequestService() {
        return mock(ItemRequestService.class);
    }

    @Bean
    public ItemService itemService() {
        return mock(ItemService.class);
    }
}
