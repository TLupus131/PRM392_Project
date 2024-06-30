package com.example.bookingapp.config;

import com.example.bookingapp.response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserAPI {
    @POST("/api/users/register")
    Call<UserResponse> register(@Body UserResponse userResponse);
}
