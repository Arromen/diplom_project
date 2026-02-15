package org.example.diplom_project_2.controller;

import org.example.diplom_project_2.entity.Booking;
import org.example.diplom_project_2.entity.Classroom;
import org.example.diplom_project_2.repository.*;
import org.example.diplom_project_2.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final ClassroomRepository classroomRepository;
    private final BookingRepository bookingRepository;
    private final GroupRepository groupRepository;
    private final BookingService bookingService;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;

    public BookingController(ClassroomRepository classroomRepository, BookingRepository bookingRepository, GroupRepository groupRepository, BookingService bookingService, TeacherRepository teacherRepository, SubjectRepository subjectRepository) {
        this.classroomRepository = classroomRepository;
        this.bookingRepository = bookingRepository;
        this.groupRepository = groupRepository;
        this.bookingService = bookingService;
        this.teacherRepository = teacherRepository;
        this.subjectRepository = subjectRepository;
    }

    @GetMapping
    public String listBookings(Model model) {
        model.addAttribute("bookings", bookingRepository.findAll());
        return "bookings/list";
    }

    @GetMapping("/new")
    public String showBookingForm(
            @RequestParam(required = false) Long classroomId,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            Model model) {

        Booking booking = new Booking();
        if (classroomId != null) {
            booking.setClassroom(new Classroom());
            booking.getClassroom().setId(classroomId);
        }
        if (startTime != null) booking.setStartTime(LocalDateTime.parse(startTime));
        if (endTime != null) booking.setEndTime(LocalDateTime.parse(endTime));

        model.addAttribute("booking", booking);
        model.addAttribute("classrooms", classroomRepository.findAll());
        model.addAttribute("groups", groupRepository.findAll());
        model.addAttribute("teachers", teacherRepository.findAll());
        model.addAttribute("subjects", subjectRepository.findAll());
        return "bookings/form";
    }

    @PostMapping
    public String createBooking(@ModelAttribute Booking booking, Model model) {
        try {
            bookingService.saveBooking(booking);
            return "redirect:/bookings";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("classrooms", classroomRepository.findAll());
            return "bookings/form"; // вернуть форму с ошибкой
        }
    }
}
