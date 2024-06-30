package com.example.bookingapp.entity;

public class Item {
    private int icon;
    private String text;
    private Class<?> toActivity;

    public Item(int icon, String text) {
        this.icon = icon;
        this.text = text;
    }

    public Item(int icon, String text, Class<?> toActivity) {
        this.icon = icon;
        this.text = text;
        this.toActivity = toActivity;
    }

    public Class<?> getToActivity() {
        return toActivity;
    }

    public void setToActivity(Class<?> toActivity) {
        this.toActivity = toActivity;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
