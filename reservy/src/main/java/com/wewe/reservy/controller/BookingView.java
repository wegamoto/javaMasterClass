package com.wewe.reservy.controller;

public class BookingView {
    private Long id;
    private String name;
    private String serviceType;
    private String date;        // เช่น 24/07/2025
    private String time;        // เช่น 14:30
    private String status;      // เช่น PENDING, COMPLETED
    private String queueNumber; // เช่น Q-001

    public BookingView(Long id, String name, String serviceType, String date, String time, String status, String queueNumber) {
        this.id = id;
        this.name = name;
        this.serviceType = serviceType;
        this.date = date;
        this.time = time;
        this.status = status;
        this.queueNumber = queueNumber;
    }

    // 👇 Getter methods (ต้องมีทั้งหมด)
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getServiceType() { return serviceType; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getStatus() { return status; }
    public String getQueueNumber() { return queueNumber; }
}
