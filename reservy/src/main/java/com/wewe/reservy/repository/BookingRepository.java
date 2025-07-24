package com.wewe.reservy.repository;

import com.wewe.reservy.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;

// repository/BookingRepository.java
public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByDateAndTime(LocalDate date, LocalTime time);
}

