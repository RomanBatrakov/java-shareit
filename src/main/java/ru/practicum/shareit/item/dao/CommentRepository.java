package ru.practicum.shareit.item.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, Integer> {
    List<Comment> findByItem_Id(int itemId);
}