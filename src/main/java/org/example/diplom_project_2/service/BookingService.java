package org.example.diplom_project_2.service;

import org.example.diplom_project_2.entity.Booking;
import org.example.diplom_project_2.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public void saveBooking(Booking booking) {
        // 1. Проверка на пустые даты
        if (booking.getStartTime() == null || booking.getEndTime() == null) {
            throw new IllegalArgumentException("Дата начала и окончания должны быть заполнены!");
        }

        // 2. Логическая проверка хронологии
        if (!booking.getEndTime().isAfter(booking.getStartTime())) {
            throw new IllegalArgumentException("Время окончания должно быть строго позже времени начала!");
        }

        // 3. Защита от бронирования в прошлом времени
        if (booking.getStartTime().isBefore(LocalDateTime.now().minusMinutes(5))) {
            throw new IllegalArgumentException("Нельзя забронировать аудиторию на прошедшее время!");
        }

        // 4. Проверка пересечений (наложений) в базе данных
        List<Booking> conflicts = bookingRepository.findOverlappingBookings(
                booking.getClassroom().getId(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getId()
        );

        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException("Аудитория уже занята в указанное время!");
        }

        bookingRepository.save(booking);
    }
}