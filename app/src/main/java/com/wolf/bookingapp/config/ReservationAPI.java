package com.wolf.bookingapp.config;

import com.wolf.bookingapp.response.CommentResponse;
import com.wolf.bookingapp.response.ReservationResponse;

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
