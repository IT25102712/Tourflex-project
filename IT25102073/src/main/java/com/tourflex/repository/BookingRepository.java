package com.tourflex.repository;

import com.tourflex.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.tourflex.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    // Email eka anuwa active bookings (Cancelled nathi) witarak ganna
    @Query("SELECT b FROM Booking b WHERE b.customerEmail = :email AND b.bookingStatus != 'Cancelled'")
    List<Booking> findActiveBookingsByEmail(@Param("email") String email);

    List<Booking> findByCustomerEmail(String customerEmail);
}