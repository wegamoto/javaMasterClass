package com.wewe.reservy.controller;

import com.wewe.reservy.model.BookingStatus;
import com.wewe.reservy.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

// controller/BookingController.java
@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/")
    public String index(Model model) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        List<BookingView> bookingViews = bookingService.getAllBookings().stream()
                .map(b -> {
                    // ตรวจสอบสถานะ หาก null ให้ใช้ PENDING เป็นค่าเริ่มต้น
                    String statusName = (b.getStatus() != null)
                            ? b.getStatus().name()
                            : BookingStatus.PENDING.name();

                    return new BookingView(
                            b.getId(),
                            b.getName(),
                            b.getServiceType(),
                            b.getDate().format(dateFormatter),
                            b.getTime().format(timeFormatter),
                            statusName,
                            String.format("Q-%03d", b.getId())
                    );
                })
                .collect(Collectors.toList());

        model.addAttribute("bookings", bookingViews);
        return "index";
    }


    @PostMapping("/book")
    public String book(@RequestParam String name,
                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
                       @RequestParam String serviceType,
                       RedirectAttributes redirectAttributes) {

        boolean success = bookingService.createBooking(name, date, time, serviceType);
        redirectAttributes.addFlashAttribute("message",
                success ? "จองคิวสำเร็จ" : "คิวนี้ถูกจองไปแล้ว");

        return "redirect:/";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id) {
        bookingService.deleteBooking(id);
        return "redirect:/";
    }
}

