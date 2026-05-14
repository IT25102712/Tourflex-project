package com.tourflex.repository;

import com.tourflex.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Integer> {
    List<Hotel> findByLocation(String location);
    Hotel findByHotelName(String hotelName);
}