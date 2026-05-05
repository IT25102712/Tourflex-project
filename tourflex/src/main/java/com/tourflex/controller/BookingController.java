package com.tourflex.controller;

import com.tourflex.model.Booking;
import com.tourflex.model.CustomPackage;
import com.tourflex.model.TourPackage;
import com.tourflex.model.User;
import com.tourflex.repository.TourPackageRepository;
import com.tourflex.service.BookingService;
import com.tourflex.service.CustomPackageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private TourPackageRepository tourPackageRepository;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private CustomPackageService customPackageService;

    // BOOK NORMAL PACKAGE - LOGIN REQUIRED
    @GetMapping("/book/{id}")
    public String showBookingPage(@PathVariable int id,
                                  Model model,
                                  HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/user/login-page";
        }

        TourPackage tourPackage = tourPackageRepository.findById(id).orElse(null);
        model.addAttribute("pkg", tourPackage);
        return "booking";
    }

    // BOOK CUSTOM PACKAGE - LOGIN REQUIRED
    @GetMapping("/custom/{id}")
    public String showCustomBookingPage(@PathVariable int id,
                                        Model model,
                                        HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/user/login-page";
        }

        CustomPackage customPackage = customPackageService.getCustomPackageById(id);
        model.addAttribute("customPkg", customPackage);
        return "booking-custom";
    }

    // SAVE BOOKING - LOGIN REQUIRED
    @PostMapping("/save")
    public String saveBooking(@RequestParam String customerName,
                              @RequestParam String customerEmail,
                              @RequestParam String bookingDate,
                              @RequestParam int numberOfPeople,
                              @RequestParam String packageName,
                              @RequestParam String location,
                              @RequestParam double totalPrice,
                              Model model,
                              HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/user/login-page";
        }

        Booking booking = new Booking();
        booking.setCustomerName(customerName);
        booking.setCustomerEmail(customerEmail);
        booking.setBookingDate(bookingDate);
        booking.setNumberOfPeople(numberOfPeople);
        booking.setPackageName(packageName);
        booking.setLocation(location);
        booking.setTotalPrice(totalPrice);

        bookingService.saveBooking(booking);

        model.addAttribute("booking", booking);
        return "booking-success";
    }

    // ADMIN VIEW ALL BOOKINGS
    @GetMapping("/list")
    public String showBookings(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("bookings", bookingService.getAllBookings());
        return "booking-list";
    }

    // ADMIN DELETE BOOKING
    @GetMapping("/delete/{id}")
    public String deleteBooking(@PathVariable int id, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        bookingService.deleteBooking(id);
        return "redirect:/booking/list";
    }

    // LOGGED-IN USER SEES THEIR OWN BOOKINGS AUTOMATICALLY
    @GetMapping("/my-bookings")
    public String showMyBookingsPage(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/user/login-page";
        }

        User user = (User) session.getAttribute("user");
        List<Booking> bookings = bookingService.getBookingsByEmail(user.getEmail());

        model.addAttribute("bookings", bookings);
        model.addAttribute("loggedInEmail", user.getEmail());
        return "my-bookings";
    }

    // LOGGED-IN USER CANCELS THEIR BOOKING
    @GetMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable int id,
                                Model model,
                                HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/user/login-page";
        }

        User user = (User) session.getAttribute("user");

        String message = bookingService.cancelBooking(id);
        List<Booking> bookings = bookingService.getBookingsByEmail(user.getEmail());

        model.addAttribute("bookings", bookings);
        model.addAttribute("loggedInEmail", user.getEmail());
        model.addAttribute("message", message);

        return "my-bookings";
    }
}