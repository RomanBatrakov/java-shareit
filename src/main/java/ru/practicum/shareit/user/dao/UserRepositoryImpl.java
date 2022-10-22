package ru.practicum.shareit.user.dao;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class UserRepositoryImpl implements UserRepository {
    private int lastId = 0;
    private final List<User> users = new ArrayList<>();

    @Override
    public User getUserById(int id) {
        return users.stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .get();
    }

    @Override
    public List<User> getAllUsers() {
        return users;
    }

    @Override
    public User createUser(User user) {
        user.setId(getId());
        users.add(user);
        return user;
    }

    @Override
    public User updateUser(int id, User user) {
        if (user.getName() != null) {
            getUserById(id).setName(user.getName());
        } else {
            user.setName(getUserById(id).getName());
        }
        if (user.getEmail() != null) {
            getUserById(id).setEmail(user.getEmail());
        } else {
            user.setEmail(getUserById(id).getEmail());
        }
        user.setId(id);
        return user;
    }

    @Override
    public void deleteUser(int id) {
        users.remove(getUserById(id));
    }

    private int getId() {
            return ++lastId;
        }
}
