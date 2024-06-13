package com.example.booking_api.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "property")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Property implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "adult_capacity", nullable = false)
    private int adultCapacity;

    @Column(name = "children_capacity", nullable = false)
    private int childrenCapacity;

    @Column(name = "pets_allowed", nullable = false)
    private boolean petsAllowed;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "discount", nullable = false)
    private Double discount;

    @Column(name = "star", nullable = false)
    private int star;

    @Column(name = "single_bed", nullable = false)
    private int singleBed;

    @Column(name = "double_bed", nullable = false)
    private int doubleBed;

    @Column(name = "bed_room", nullable = false)
    private int bedRoom;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @ManyToOne
    @JsonIgnoreProperties("properties")
    @JoinColumn(name = "region_id", referencedColumnName = "id")
    private Region region;

    @JsonIgnore
    @ManyToMany(mappedBy = "properties")
    private List<Convenient> convenients;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("property")
    private List<PropertyImg> propertyImgs;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("property")
    private List<Reservation> reservations;
}
