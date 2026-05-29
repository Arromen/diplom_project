package org.example.diplom_project_2.controller;

import org.example.diplom_project_2.entity.Booking;
import org.example.diplom_project_2.entity.Group;
import org.example.diplom_project_2.entity.Teacher;
import org.example.diplom_project_2.repository.BookingRepository;
import org.example.diplom_project_2.repository.GroupRepository;
import org.example.diplom_project_2.repository.TeacherRepository;
import org.example.diplom_project_2.util.WeekUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ScheduleController {

    private final GroupRepository groupRepository;
    private final BookingRepository bookingRepository;
    private final TeacherRepository teacherRepository;

    public ScheduleController(GroupRepository groupRepository, BookingRepository bookingRepository, TeacherRepository teacherRepository) {
        this.groupRepository = groupRepository;
        this.bookingRepository = bookingRepository;
        this.teacherRepository = teacherRepository;
    }

    @GetMapping("/schedule")
    public String listGroups(Model model) {
        model.addAttribute("groups", groupRepository.findAll());
        return "schedule/groups";
    }

    @GetMapping("/schedule/group/{id}")
    public String viewGroupSchedule(@PathVariable Long id, Model model) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Группа не найдена"));

        List<Booking> lessons = bookingRepository.findByGroupAndBookingType(group, "lesson");

        model.addAttribute("group", group);
        model.addAttribute("lessons", lessons);
        return "schedule/group-schedule";
    }

    @PostMapping("/groups")
    public String createGroup(@RequestParam String name, RedirectAttributes redirectAttrs) {
        if (name != null && !name.trim().isEmpty() && !groupRepository.existsByName(name)) {
            groupRepository.save(new Group(null, name));
        }
        return "redirect:/schedule";
    }

    @GetMapping("/schedule/teachers")
    public String listTeachers(Model model) {
        model.addAttribute("teachers", teacherRepository.findAll());
        return "schedule/teachers";
    }

    @GetMapping("/schedule/teacher/{id}")
    public String viewTeacherSchedule(@PathVariable Long id, Model model) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Преподаватель не найден"));

        List<Booking> lessons = bookingRepository.findByTeacherAndBookingType(teacher, "lesson");

        model.addAttribute("teacher", teacher);
        model.addAttribute("lessons", lessons);
        return "schedule/teacher-schedule";
    }

    @GetMapping("/schedule/group/{id}/week")
    public String viewGroupWeekSchedule(@PathVariable Long id, Model model) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Группа не найдена"));

        LocalDate monday = WeekUtils.getCurrentMonday();
        List<LocalDate> weekDates = WeekUtils.getWeekDates(monday);

        // ИСПРАВЛЕНО: Вычисляем временные границы недели для SQL
        LocalDateTime startOfWeek = monday.atStartOfDay();
        LocalDateTime endOfWeek = monday.plusDays(6).atTime(23, 59, 59);

        // Тянем из базы только целевые строки за текущую неделю
        List<Booking> thisWeekLessons = bookingRepository.findByGroupAndBookingTypeAndStartTimeBetween(
                group, "lesson", startOfWeek, endOfWeek
        );

        model.addAttribute("group", group);
        model.addAttribute("weekDates", weekDates);
        model.addAttribute("lessons", thisWeekLessons);
        model.addAttribute("monday", monday);

        return "schedule/group-week-schedule";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Возвращает имя нашего файла login.html
    }
}