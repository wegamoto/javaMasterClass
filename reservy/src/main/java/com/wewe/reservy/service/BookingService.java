package com.wewe.reservy.service;

import com.wewe.reservy.model.Booking;
import com.wewe.reservy.model.BookingStatus;
import com.wewe.reservy.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public boolean createBooking(String name, LocalDate date, LocalTime time, String serviceType) {
        // ตรวจสอบว่ามีคิวซ้ำเวลานั้นไหม
        if (bookingRepository.existsByDateAndTime(date, time)) {
            return false;
        }

        Booking booking = new Booking();
        booking.setName(name);
        booking.setDate(date);
        booking.setTime(time);
        booking.setServiceType(serviceType);
        booking.setStatus(BookingStatus.PENDING); // สถานะเริ่มต้น

        // สร้างเลขคิว เช่น Q-240725-001
        long countToday = bookingRepository.countByDate(date) + 1;
        String formattedDate = date.format(DateTimeFormatter.ofPattern("ddMMyy"));
        String queueNumber = String.format("Q-%s-%03d", formattedDate, countToday);
        booking.setQueueNumber(queueNumber);

        bookingRepository.save(booking);
        return true;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}
