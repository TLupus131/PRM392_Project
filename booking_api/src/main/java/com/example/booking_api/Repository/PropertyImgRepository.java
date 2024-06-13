package com.example.booking_api.Repository;

import com.example.booking_api.Models.PropertyImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyImgRepository extends JpaRepository<PropertyImg, Integer> {
}
