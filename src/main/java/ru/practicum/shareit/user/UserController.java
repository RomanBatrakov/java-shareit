package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {

    private final UserService service;

//    @GetMapping("/{id}")
//    public ResponseEntity<UserDto> getUserById(@PathVariable int id) {
//        return ResponseEntity.ok(service.getUserById(id));
//    }

//    @GetMapping
//    public ResponseEntity<List<UserDto>> getAllUsers() {
//        return ResponseEntity.ok(service.getAllUsers());
//    }

//    @PostMapping
//    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
//        return ResponseEntity.ok(service.createUser(userDto));
//    }
//
//    @PatchMapping("/{id}")
//    public ResponseEntity<UserDto> updateUser(@PathVariable int id,
//                                              @RequestBody UserDto userDto) {
//        return ResponseEntity.ok(service.updateUser(id, userDto));
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteUser(@PathVariable int id) {
//        service.deleteUser(id);
//    }
}