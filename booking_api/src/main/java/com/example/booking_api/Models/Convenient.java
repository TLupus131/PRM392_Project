package com.example.booking_api.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "convenient")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Convenient implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "property_convenient",
            joinColumns = { @JoinColumn(name = "convenient_id") },
            inverseJoinColumns = { @JoinColumn(name = "property_id") }
    )
    @JsonManagedReference
    private List<Property> properties;

}
