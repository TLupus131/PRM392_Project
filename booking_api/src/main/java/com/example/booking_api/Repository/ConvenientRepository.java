package com.example.booking_api.Repository;

import com.example.booking_api.Models.Convenient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConvenientRepository extends JpaRepository<Convenient, Integer> {
}
