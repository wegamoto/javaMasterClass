package com.wewe.reservy.controller;

import java.time.LocalTime;

public class BookingView {
    private Long id;
    private String name;
    private String date; // เก็บวันที่เป็น String (format dd/MM/yyyy)
    private LocalTime time;

    public BookingView(Long id, String name, String date, LocalTime time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    // getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDate() { return date; }
    public LocalTime getTime() { return time; }
}
