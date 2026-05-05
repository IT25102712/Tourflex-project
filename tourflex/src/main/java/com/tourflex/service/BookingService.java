package com.tourflex.service;

import com.tourflex.model.Booking;
import com.tourflex.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public Booking saveBooking(Booking booking) {
        booking.setBookingStatus("Active");
        booking.setRefundStatus("Not Requested");
        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public void deleteBooking(int id) {
        bookingRepository.deleteById(id);
    }

    public List<Booking> getBookingsByEmail(String email) {
        return bookingRepository.findByCustomerEmail(email);
    }

    public Booking getBookingById(int id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public String cancelBooking(int id) {
        Booking booking = bookingRepository.findById(id).orElse(null);

        if (booking == null) {
            return "Booking not found!";
        }

        if ("Cancelled".equals(booking.getBookingStatus())) {
            return "This booking is already cancelled.";
        }

        LocalDate today = LocalDate.now();
        LocalDate tripDate = LocalDate.parse(booking.getBookingDate());

        long daysBetween = ChronoUnit.DAYS.between(today, tripDate);

        if (daysBetween >= 5) {
            booking.setBookingStatus("Cancelled");
            booking.setRefundStatus("Pending");
            bookingRepository.save(booking);
            return "Booking cancelled successfully. Refund status: Pending";
        } else {
            return "Cancellation not allowed. You can cancel only 5 days before the trip date.";
        }
    }

    public List<String> getTopBookedPackageNames() {
        List<Booking> allBookings = bookingRepository.findAll();

        Map<String, Integer> packageCount = new HashMap<>();

        for (Booking booking : allBookings) {
            if ("Active".equals(booking.getBookingStatus())) {
                String packageName = booking.getPackageName();
                packageCount.put(packageName, packageCount.getOrDefault(packageName, 0) + 1);
            }
        }

        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(packageCount.entrySet());
        sortedList.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        List<String> topPackageNames = new ArrayList<>();

        for (int i = 0; i < sortedList.size() && i < 3; i++) {
            topPackageNames.add(sortedList.get(i).getKey());
        }

        return topPackageNames;
    }
}