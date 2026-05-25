package org.example.diplom_project_2.repository;

import org.example.diplom_project_2.entity.Booking;
import org.example.diplom_project_2.entity.Group;
import org.example.diplom_project_2.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Добавлена проверка (:id IS NULL OR b.id <> :id) для безопасного редактирования
    @Query("""
        SELECT b FROM Booking b 
        WHERE b.classroom.id = :classroomId 
          AND b.startTime < :endTime 
          AND b.endTime > :startTime
          AND (:id IS NULL OR b.id <> :id)
    """)
    List<Booking> findOverlappingBookings(
            @Param("classroomId") Long classroomId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("id") Long id
    );

    List<Booking> findByGroupAndBookingType(Group group, String bookingType);

    // НОВЫЙ МЕТОД: переносит фильтрацию недельного расписания из памяти сервера в SQL-запрос
    List<Booking> findByGroupAndBookingTypeAndStartTimeBetween(
            Group group,
            String bookingType,
            LocalDateTime start,
            LocalDateTime end
    );

    List<Booking> findByTeacherAndBookingType(Teacher teacher, String bookingType);
}
