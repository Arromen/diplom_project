package org.example.diplom_project_2.service;

import org.example.diplom_project_2.entity.Booking;
import org.example.diplom_project_2.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public void saveBooking(Booking booking) {
        // Проверяем, не пересекается ли новое бронирование с существующими
        List<Booking> conflicts = bookingRepository.findOverlappingBookings(
                booking.getClassroom().getId(),
                booking.getStartTime(),
                booking.getEndTime()
        );

        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException("Аудитория уже занята в указанное время!");
        }

        bookingRepository.save(booking);
    }
}
