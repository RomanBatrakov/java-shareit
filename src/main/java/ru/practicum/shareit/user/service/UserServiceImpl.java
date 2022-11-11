package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto getUserById(int id) {
        try {
            return userMapper.toUserDto(userRepository.findById(id).get());
        } catch (NotFoundException e) {
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
        } catch (NotFoundException e) {
            throw new ValidationException("Ошибка входящих данных");
        }
    }

    @Override
    public UserDto updateUser(int id, UserDto userDto) {
        try {
            User user = userMapper.toUser(userDto);
            User userFromDb = userRepository.findById(id).get();
            if (user.getName() != null) {
                userFromDb.setName(user.getName());
            }
            if (user.getEmail() != null) {
                userFromDb.setEmail(user.getEmail());
            }
            return userMapper.toUserDto(userRepository.save(userFromDb));
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