package org.example.diplom_project_2.controller;

import org.example.diplom_project_2.entity.Booking;
import org.example.diplom_project_2.entity.Group;
import org.example.diplom_project_2.repository.BookingRepository;
import org.example.diplom_project_2.repository.GroupRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingApiController {

    private final BookingRepository bookingRepository;
    private final GroupRepository groupRepository;

    public BookingApiController(BookingRepository bookingRepository, GroupRepository groupRepository) {
        this.bookingRepository = bookingRepository;
        this.groupRepository = groupRepository;
    }

    // Эндпоинт, который снабжает FullCalendar данными при смене недель/месяцев
    @GetMapping("/group/{groupId}")
    public List<Map<String, Object>> getGroupBookingsForCalendar(
            @PathVariable Long groupId,
            @RequestParam String start,
            @RequestParam String end) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Группа не найдена"));

        // Парсим ISO-даты, которые присылает FullCalendar (отрезаем таймзону для LocalDateTime)
        LocalDateTime startTime = java.time.OffsetDateTime.parse(start).toLocalDateTime();
        LocalDateTime endTime = java.time.OffsetDateTime.parse(end).toLocalDateTime();

        List<Booking> lessons = bookingRepository.findByGroupAndBookingTypeAndStartTimeBetween(
                group, "lesson", startTime, endTime
        );

        List<Map<String, Object>> events = new ArrayList<>();
        for (Booking b : lessons) {
            Map<String, Object> event = new HashMap<>();
            event.put("id", b.getId());

            // Красивый заголовок карточки: Предмет (Аудитория)
            String subjectName = b.getSubject() != null ? b.getSubject().getName() : b.getPurpose();
            event.put("title", subjectName + " (ауд. " + b.getClassroom().getName() + ")");

            event.put("start", b.getStartTime().toString());
            event.put("end", b.getEndTime().toString());

            // Доп. информация (ФИО преподавателя) при клике
            event.put("description", b.getTeacher() != null ? b.getTeacher().getFullName() : "Преподаватель не указан");
            event.put("color", "#0d6efd"); // Фирменный синий цвет Bootstrap для карточек

            events.add(event);
        }
        return events;
    }
}
