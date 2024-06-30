package com.example.bookingapp.config;

import com.example.bookingapp.response.CommentResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CommentAPI {
    @POST("/api/comments/add")
    Call<CommentResponse> add(@Body CommentResponse commentResponse);
}
