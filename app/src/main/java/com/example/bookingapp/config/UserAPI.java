package com.example.bookingapp.config;

import com.example.bookingapp.response.UserResponse;

import java.sql.Date;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserAPI {
    @POST("/api/users/register")
    Call<UserResponse> register(@Body UserResponse userResponse);

    @PUT("/api/users/update")
    Call<Void> update(
            @Query("id") int id,
            @Query("name") String name,
            @Query("email") String email,
            @Query("password") String password,
            @Query("address") String address,
            @Query("phone") String phone,
            @Query("nationality") String nationality,
            @Query("dob") String dob,
            @Query("gender") String gender
    );
}
