package com.example.booking_api.Repository;

import com.example.booking_api.Models.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer> {

    @Query(value = "SELECT p.*" +
            "FROM property p\n" +
            "JOIN region r ON r.id = p.region_id\n" +
            "LEFT JOIN reservation res ON res.property_id = p.id\n" +
            "WHERE r.name = :region\n" +
            "  AND p.adult_capacity >= :adult\n" +
            "  AND p.children_capacity >= :children\n" +
            "  AND p.pets_allowed = :petAllow" +
            "  AND p.quantity - (\n" +
            "    SELECT COALESCE(SUM(r.quantity), 0) \n" +
            "    FROM reservation r \n" +
            "    WHERE r.property_id = p.id \n" +
            "      AND (r.check_in_date BETWEEN :checkInDate AND :checkOutDate\n" +
            "      OR r.check_out_date BETWEEN :checkInDate AND :checkOutDate)\n" +
            "  ) >= :room\n" +
            "GROUP BY p.id;", nativeQuery = true)
    List<Property> getPropertiesByRequest(@Param("region") String region,@Param("checkInDate") Date checkInDate,@Param("checkOutDate") Date checkOutDate,@Param("room") int room,@Param("adult") int adult,@Param("children") int children, @Param("petAllow") boolean petAllow);
}
