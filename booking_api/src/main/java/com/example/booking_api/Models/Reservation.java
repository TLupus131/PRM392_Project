package com.example.booking_api.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "reservation")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "property_id", referencedColumnName = "id")
    private Property property;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "status", nullable = false)
    private boolean status;

    @Temporal(TemporalType.DATE)
    @Column(name = "check_in_date")
    private Date checkInDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "check_out_date")
    private Date checkOutDate;

    @Column(name = "staff_evaluation")
    private Double staffEvaluation;

    @Column(name = "clean_evaluation")
    private Double cleanEvaluation;

    @Column(name = "comfortable_evaluation")
    private Double comfortableEvaluation;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("reservation")
    private ReservationDetail reservationDetail;
}
