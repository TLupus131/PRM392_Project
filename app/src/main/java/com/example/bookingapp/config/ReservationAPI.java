package com.example.bookingapp.config;

import com.example.bookingapp.response.CommentResponse;
import com.example.bookingapp.response.ReservationResponse;

import java.sql.Date;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReservationAPI {
    @POST("/api/reservations/add")
    Call<ReservationResponse> add(@Body ReservationResponse reservationResponse);

    @POST("/api/reservations/delete/{reservationId}")
    Call<Void> delete(@Path("reservationId") int reservationId);

    @PUT("/api/reservations/update")
    Call<Void> update(
            @Query("id") int reservationId,
            @Query("checkInDate") Date checkInDate,
            @Query("checkOutDate") Date checkOutDate
    );
}
