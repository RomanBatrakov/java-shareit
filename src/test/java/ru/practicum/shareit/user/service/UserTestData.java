package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

public class UserTestData {
    public static final UserDto userDto1 = UserDto.builder().id(1).name("user1").email("user1@mail.ru").build();
    public static final UserDto userDto2 = UserDto.builder().id(2).name("user2").email("user2@mail.ru").build();
    public static final UserDto userDto3 = UserDto.builder().id(3).name("user3").email("user3@mail.ru").build();
    public static final UserDto userDtoCreated = UserDto.builder().id(4).name("userCreated")
            .email("userCreated@mail.ru").build();
    public static final UserDto userDtoWrongCreated = UserDto.builder().name("userDtoWrongCreated")
            .email("user1@mail.ru").build();
}