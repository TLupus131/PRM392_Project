package com.example.bookingapp.entity;

public class FutureTrip {

    private final String name;
    private final String date;
    private final String status;

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

