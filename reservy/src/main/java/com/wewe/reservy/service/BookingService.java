package com.wewe.reservy.service;

import com.wewe.reservy.model.Booking;
import com.wewe.reservy.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// service/BookingService.java
@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepo;

    public boolean createBooking(String name, LocalDate date, LocalTime time) {
        if (bookingRepo.existsByDateAndTime(date, time)) {
            return false;
        }
        Booking booking = new Booking();
        booking.setName(name);
        booking.setDate(date);
        booking.setTime(time);
        bookingRepo.save(booking);
        return true;
    }

    public List<Booking> getAllBookings() {
        return bookingRepo.findAll();
    }

    public void deleteBooking(Long id) {
        bookingRepo.deleteById(id);
    }
}

