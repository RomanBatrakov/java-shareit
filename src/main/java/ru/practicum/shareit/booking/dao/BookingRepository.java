package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByBooker_IdOrderByStartDesc(int userId);

    List<Booking> findByBooker_IdAndEndBeforeOrderByStartDesc(int userId, LocalDateTime now);

    List<Booking> findByBooker_IdAndEndAfterAndStartBeforeOrderByStartDesc(int userId,
                                                                           LocalDateTime now, LocalDateTime now1);

    List<Booking> findByBooker_IdAndStartAfterOrderByStartDesc(int userId, LocalDateTime now);

    List<Booking> findByBooker_IdAndStatusOrderByStartDesc(int userId, BookingStatus status);

    List<Booking> findByItem_OwnerIdOrderByStartDesc(int ownerId);

    List<Booking> findByItem_OwnerIdAndEndAfterAndStartBeforeOrderByStartDesc(int ownerId,
                                                                              LocalDateTime now, LocalDateTime now1);

    List<Booking> findByItem_OwnerIdAndEndBeforeOrderByStartDesc(int ownerId, LocalDateTime now);

    List<Booking> findByItem_OwnerIdAndStartAfterOrderByStartDesc(int ownerId, LocalDateTime now);

    List<Booking> findByItem_OwnerIdAndStatusOrderByStartDesc(int ownerId, BookingStatus waiting);
}