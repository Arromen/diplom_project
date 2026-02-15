package org.example.diplom_project_2.controller;

import org.example.diplom_project_2.entity.Classroom;
import org.example.diplom_project_2.repository.ClassroomRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Controller
public class SearchController {

    private final ClassroomRepository classroomRepository;

    public SearchController(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    @GetMapping("/search")
    public String showSearchForm() {
        return "search/form";
    }

    @GetMapping("/search/results")
    public String searchAvailable(
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam(defaultValue = "1") int minCapacity,
            Model model) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime start = LocalDateTime.parse(startTime, formatter);
            LocalDateTime end = LocalDateTime.parse(endTime, formatter);

            if (!end.isAfter(start)) {
                model.addAttribute("error", "Время окончания должно быть позже начала");
                return "search/form";
            }

            List<Classroom> available = classroomRepository.findAvailableClassrooms(start, end, minCapacity);
            model.addAttribute("availableClassrooms", available);
            model.addAttribute("startTime", startTime);
            model.addAttribute("endTime", endTime);
            model.addAttribute("minCapacity", minCapacity);

        } catch (Exception e) {
            model.addAttribute("error", "Неверный формат даты/времени");
        }

        return "search/results";
    }
}
