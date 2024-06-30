package com.example.bookingapp.entity;

public class UserManager {
    private static UserManager instance;
    private User authUser;

    private UserManager() {}

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void setAuthUser(User user) {
        this.authUser = user;
    }

    public User getAuthUser() {
        return authUser;
    }
}
