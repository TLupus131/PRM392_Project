package com.wolf.bookingapp.entity;

import java.io.Serializable;
import java.sql.Date;

public class User implements Serializable {
    private int id;
    private String name;
    private String avatar;
    private String email;
    private String address;
    private String nationality;
    private String phone;
    private String password;
    private boolean gender;
    private String dob;
    private String role;

    public User(int id, String name, String avatar, String email, String address, String nationality, String phone, boolean gender, String dob, String role) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.email = email;
        this.address = address;
        this.nationality = nationality;
        this.phone = phone;
        this.gender = gender;
        this.dob = dob;
        this.role = role;
    }

    public User(int id, String name, String avatar, String email, String password, String address, String nationality, String phone, boolean gender, String dob, String role) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.password = password;
        this.email = email;
        this.address = address;
        this.nationality = nationality;
        this.phone = phone;
        this.gender = gender;
        this.dob = dob;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
