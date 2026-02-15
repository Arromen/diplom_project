package org.example.diplom_project_2.repository;

import org.example.diplom_project_2.entity.Booking;
import org.example.diplom_project_2.entity.Group;
import org.example.diplom_project_2.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("""
        SELECT b FROM Booking b 
        WHERE b.classroom.id = :classroomId 
          AND b.startTime < :endTime 
          AND b.endTime > :startTime
    """)
    List<Booking> findOverlappingBookings(Long classroomId, LocalDateTime startTime, LocalDateTime endTime);
    List<Booking> findByGroupAndBookingType(Group group, String bookingType);
    List<Booking> findByTeacherAndBookingType(Teacher teacher, String bookingType);
}
