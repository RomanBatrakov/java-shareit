package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto getUserById(long id) {
        try {
            return userMapper.toUserDto(userRepository.findById(id).get());
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        try {
            return userMapper.toUserDto(userRepository.save(userMapper.toUser(userDto)));
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException("Ошибка входящих данных");
        }
    }

    @Override
    public UserDto updateUser(long id, UserDto userDto) {
        try {
            User user = userMapper.toUser(userDto);
            User userFromDb = userRepository.findById(id).get();
            Optional.ofNullable(user.getName()).ifPresent(userFromDb::setName);
            Optional.ofNullable(user.getEmail()).ifPresent(userFromDb::setEmail);
            return userMapper.toUserDto(userRepository.save(userFromDb));
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    public void deleteUser(long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь не найден");
        }
    }
}