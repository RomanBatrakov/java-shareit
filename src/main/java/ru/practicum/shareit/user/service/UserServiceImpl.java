package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(int id) {
        try {
            return userRepository.findById(id);
        } catch (NotFoundException e) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User createUser(User user) {
        try {
            return userRepository.save(user);
        } catch (NotFoundException e) {
            throw new ValidationException("Ошибка входящих данных");
        }
    }

    @Override
    @Transactional
    public User updateUser(int id, User user) {
        try {
            return userRepository.updateUser(id, user);
        } catch (NotFoundException e) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        try {
            userRepository.deleteById(id);
        } catch (NotFoundException e) {
            throw new NotFoundException("Пользователь не найден");
        }
    }
}