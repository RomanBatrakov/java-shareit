package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.user.service.UserTestData.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceImplTest {
    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService.createUser(userDto1);
        userService.createUser(userDto2);
        userService.createUser(userDto3);
    }

    @Test
    void getUserByIdTest() {
        UserDto userDtoFromSQL = userService.getUserById(1);
        assertThat(userDtoFromSQL.getName(), equalTo(userDto1.getName()));
    }

    @Test
    void getUserByWrongIdTest() {
        assertThrows(NotFoundException.class, () -> userService.getUserById(100));
    }

    @Test
    void getAllUsersTest() {
        List<UserDto> users = userService.getAllUsers();
        assertThat(users.size(), equalTo(3));
        userService.createUser(userDtoCreated);
        assertThat(userService.getAllUsers().size(), equalTo(4));
    }

    @Test
    void createUserTest() {
        userService.createUser(userDtoCreated);
        List<UserDto> users = userService.getAllUsers();
        assertThat(users.get(3).getName(), equalTo(userDtoCreated.getName()));
        assertThat(users.get(3).getEmail(), equalTo(userDtoCreated.getEmail()));
    }

    @Test
    void createUserWithDuplicateEmailTest() {
        assertThrows(ValidationException.class, () -> userService.createUser(userDtoWrongCreated));
    }

    @Test
    void updateUserTest() {
        userDto2.setName("User2new");
        userDto2.setEmail("User2new@mail.ru");
        UserDto userDtoFromSQL = userService.updateUser(2, userDto2);
        assertThat(userDtoFromSQL.getName(), equalTo(userDto2.getName()));
        assertThat(userDtoFromSQL.getEmail(), equalTo(userDto2.getEmail()));
        userDto2.setName("user2");
        userDto2.setEmail("user2@mail.ru");
    }

    @Test
    void updateUserTestWhenNotFound() {
        assertThrows(NotFoundException.class, () -> userService.updateUser(20, userDto2));
    }

    @Test
    void deleteUserTest() {
        userService.deleteUser(1);
        assertThat(userService.getAllUsers().size(), equalTo(2));
    }

    @Test
    void deleteUserWrongIdTest() {
        assertThrows(NotFoundException.class, () -> userService.deleteUser(100));
    }
}

//        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
//        User user = query.setParameter("email", userDto.getEmail())
//                .getSingleResult();