package com.example.booking_api.Service;

import com.example.booking_api.Models.Region;
import com.example.booking_api.Repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    public List<Region> allRegion() {
        return regionRepository.findAll();
    }
}
