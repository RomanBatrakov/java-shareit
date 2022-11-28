package ru.practicum.shareit.booking.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends PagingAndSortingRepository<Booking, Integer> {
    List<Booking> findByItem_IdOrderByStartDesc(int itemId);

    Page<Booking> findByBooker_IdOrderByStartDesc(int userId, Pageable pageable);

    Page<Booking> findByBooker_IdAndEndBeforeOrderByStartDesc(int userId, LocalDateTime now, Pageable pageable);

    Page<Booking> findByBooker_IdAndEndAfterAndStartBeforeOrderByStartDesc(
            int userId, LocalDateTime now, LocalDateTime now1, Pageable pageable);

    Page<Booking> findByBooker_IdAndStartAfterOrderByStartDesc(int userId, LocalDateTime now, Pageable pageable);

    Page<Booking> findByBooker_IdAndStatusOrderByStartDesc(int userId, BookingStatus status, Pageable pageable);

    Page<Booking> findByItem_OwnerIdOrderByStartDesc(int ownerId, Pageable pageable);

    Page<Booking> findByItem_OwnerIdAndEndAfterAndStartBeforeOrderByStartDesc(
            int ownerId, LocalDateTime now, LocalDateTime now1, Pageable pageable);

    Page<Booking> findByItem_OwnerIdAndEndBeforeOrderByStartDesc(int ownerId, LocalDateTime now, Pageable pageable);

    Page<Booking> findByItem_OwnerIdAndStartAfterOrderByStartDesc(int ownerId, LocalDateTime now, Pageable pageable);

    Page<Booking> findByItem_OwnerIdAndStatusOrderByStartDesc(int ownerId, BookingStatus waiting, Pageable pageable);

    List<Booking> findByItem_IdAndBooker_IdAndStatusAndEndBefore(
            int itemId, int userId, BookingStatus status, LocalDateTime now);
}