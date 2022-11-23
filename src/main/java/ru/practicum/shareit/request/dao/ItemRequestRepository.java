package ru.practicum.shareit.request.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.List;

@Repository
public interface ItemRequestRepository extends PagingAndSortingRepository<ItemRequest, Integer> {
    List<ItemRequest> findByRequestor_Id(int userId);

    Page<ItemRequest> findByRequestorNotLike(User user, Pageable pageable);
}