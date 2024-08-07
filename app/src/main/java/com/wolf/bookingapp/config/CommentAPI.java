package com.wolf.bookingapp.config;

import com.wolf.bookingapp.response.CommentResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CommentAPI {
    @POST("/api/comments/add")
    Call<CommentResponse> add(@Body CommentResponse commentResponse);
}
