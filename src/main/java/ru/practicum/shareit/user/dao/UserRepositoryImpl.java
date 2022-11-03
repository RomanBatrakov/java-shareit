package ru.practicum.shareit.user.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;
@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final UserRepository userRepository;

    public UserRepositoryImpl(@Lazy UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User updateUser(int id, User user) {
        User userFromDb = userRepository.findById(id).get();
        if (user.getName() != null) {
            userFromDb.setName(user.getName());
        }
        if (user.getEmail() != null) {
            userFromDb.setEmail(user.getEmail());
        }
        return userRepository.save(userFromDb);
    }
}
