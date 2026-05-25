package org.example.diplom_project_2.repository;

import org.example.diplom_project_2.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {

    @Query("""
        SELECT DISTINCT c FROM Classroom c
        WHERE c.id NOT IN (
            SELECT b.classroom.id FROM Booking b
            WHERE b.startTime < :endTime AND b.endTime > :startTime
        )
        AND c.capacity >= :minCapacity
    """)
    List<Classroom> findAvailableClassrooms(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("minCapacity") int minCapacity
    );
}
