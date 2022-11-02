package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.User;

import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<User> updateUser(int id, User user);
}
