package com.example.booking_api.Service;

import com.example.booking_api.Models.Property;
import com.example.booking_api.Repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    public List<Property> allProperty() {
        return propertyRepository.findAll();
    }

    public List<Property> getRequestProperty(String location, Date checkInDate, Date checkOutDate, int room, int adult, int children, boolean petAllow){
        return propertyRepository.getPropertiesByRequest(location, checkInDate, checkOutDate, room, adult, children, petAllow);
    }
}
