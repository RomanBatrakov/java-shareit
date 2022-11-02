package ru.practicum.shareit.user.dao;

import org.springframework.context.annotation.Lazy;
import ru.practicum.shareit.user.User;

import java.util.Optional;

public class UserRepositoryImpl implements UserRepositoryCustom{
    private final UserRepository userRepository;

    public UserRepositoryImpl(@Lazy UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public Optional<User> updateUser(int id, User user) {

        return Optional.empty();
    }
}
