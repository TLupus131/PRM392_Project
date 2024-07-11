package com.wolf.bookingapp.response;

public class CommentResponse {
    private String content;
    private int userId;
    private int propertyId;
    private String message;

    public CommentResponse(String content, int userId, int propertyId) {
        this.content = content;
        this.userId = userId;
        this.propertyId = propertyId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }
}
