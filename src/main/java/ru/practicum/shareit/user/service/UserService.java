package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(int id);

    List<User> getAllUsers();

    User createUser(User user);

    User updateUser(int id, User user);

    void deleteUser(int id);
}
