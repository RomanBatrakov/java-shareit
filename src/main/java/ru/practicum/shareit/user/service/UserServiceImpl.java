package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<User> getUserById(int id) {
        try {
            return userRepository.findById(id);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        if (userValidation(user)) {
            return userRepository.save(user);
        } else {
            throw new ValidationException("Ошибка входящих данных");
        }
    }

    @Override
    public Optional<User> updateUser(int id, User user) {
        try {
            if (userValidation(user)) {
                return userRepository.updateUser(id, user);
            } else {
                throw new ValidationException("Ошибка входящих данных");
            }
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    public void deleteUser(int id) {
        try {
            userRepository.deleteById(id);
//            itemRepository.deleteOwnerItems(id);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    private boolean userValidation(User user) {
        return getAllUsers().stream()
                .noneMatch(x -> x.getEmail().equals(user.getEmail()) && !(x.getId() == user.getId()));
    }
}
