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
public interface BookingRepository extends PagingAndSortingRepository<Booking, Long> {
    List<Booking> findByItem_IdOrderByStartDesc(long itemId);

    Page<Booking> findByBooker_IdOrderByStartDesc(long userId, Pageable pageable);

    Page<Booking> findByBooker_IdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime now, Pageable pageable);

    Page<Booking> findByBooker_IdAndEndAfterAndStartBeforeOrderByStartDesc(
            long userId, LocalDateTime now, LocalDateTime now1, Pageable pageable);

    Page<Booking> findByBooker_IdAndStartAfterOrderByStartDesc(long userId, LocalDateTime now, Pageable pageable);

    Page<Booking> findByBooker_IdAndStatusOrderByStartDesc(long userId, BookingStatus status, Pageable pageable);

    Page<Booking> findByItem_OwnerIdOrderByStartDesc(long ownerId, Pageable pageable);

    Page<Booking> findByItem_OwnerIdAndEndAfterAndStartBeforeOrderByStartDesc(
            long ownerId, LocalDateTime now, LocalDateTime now1, Pageable pageable);

    Page<Booking> findByItem_OwnerIdAndEndBeforeOrderByStartDesc(long ownerId, LocalDateTime now, Pageable pageable);

    Page<Booking> findByItem_OwnerIdAndStartAfterOrderByStartDesc(long ownerId, LocalDateTime now, Pageable pageable);

    Page<Booking> findByItem_OwnerIdAndStatusOrderByStartDesc(long ownerId, BookingStatus waiting, Pageable pageable);

    List<Booking> findByItem_IdAndBooker_IdAndStatusAndEndBefore(
            long itemId, long userId, BookingStatus status, LocalDateTime now);
}