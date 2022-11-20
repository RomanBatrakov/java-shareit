package ru.practicum.shareit.request.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserRepository;

import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRequestRepositoryTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    private ItemRequest itemRequest = ItemRequest.builder()
            .description("description")
            .requestor(User.builder()
                    .id(1)
                    .name("John")
                    .email("john.doe@mail.com")
                    .build())
            .created(LocalDateTime.now())
            .build();

    @Test
    void contextLoads() {
        assertNotNull(em);
    }

    @Test
    void findByRequestor_Id() {
    }

    @Test
    void findByRequestorNotLike() {
    }
}