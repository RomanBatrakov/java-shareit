package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getUserById(int id) {
        try {
            return userRepository.findById(id);
        } catch (NotFoundException e) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        try {
            return userRepository.save(user);
        } catch (NotFoundException e) {
            throw new ValidationException("Ошибка входящих данных");
        }
    }

    @Override
    public User updateUser(int id, User user) {
        try {
                User userFromDb = userRepository.findById(id).get();
                if (user.getName() != null) {
                    userFromDb.setName(user.getName());
                }
                if (user.getEmail() != null) {
                    userFromDb.setEmail(user.getEmail());
                }
                return userRepository.save(userFromDb);
        } catch (NotFoundException e) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    public void deleteUser(int id) {
        try {
            userRepository.deleteById(id);
        } catch (NotFoundException e) {
            throw new NotFoundException("Пользователь не найден");
        }
    }
}