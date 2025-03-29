package com.wewe.earthquake.model;

import java.util.Date;

public class Earthquake {
    private String title;
    private String description;
    private Date date;

    public Earthquake(String title, String description, Date date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }
}

