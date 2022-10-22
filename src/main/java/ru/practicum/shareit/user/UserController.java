package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable int id) {
        return UserMapper.toUserDto(service.getUserById(id));
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return service.getAllUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        return UserMapper.toUserDto(service.createUser(UserMapper.toUser(userDto)));
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable int id,
                              @RequestBody UserDto userDto) {
        return UserMapper.toUserDto(service.updateUser(id, UserMapper.toUser(userDto)));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        service.deleteUser(id);
    }
}
