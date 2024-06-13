package com.example.bookingapp.entity;

public class FutureTrip {

    private String name;
    private String date;
    private String status;

    public FutureTrip(String name, String date, String status) {
        this.name = name;
        this.date = date;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}

