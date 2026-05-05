package com.tourflex.service;

import com.tourflex.model.Hotel;
import com.tourflex.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    public Hotel saveHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    public List<Hotel> getHotelsByLocation(String location) {
        return hotelRepository.findByLocation(location);
    }

    public void deleteHotel(int id) {
        hotelRepository.deleteById(id);
    }

    public Hotel getHotelById(int id) {
        return hotelRepository.findById(id).orElse(null);
    }
}