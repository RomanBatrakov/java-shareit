package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserService {
    User getUserById(int id);

    List<User> getAllUsers();

    User createUser(User user);

    User updateUser(int id, User user);

    void deleteUser(int id);
}
