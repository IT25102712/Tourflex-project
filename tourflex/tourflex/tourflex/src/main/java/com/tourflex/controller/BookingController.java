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

    // 1. BOOK NORMAL PACKAGE
    @GetMapping("/book/{id}")
    public String showBookingPage(@PathVariable int id, Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/user/login-page";
        }
        User user = (User) session.getAttribute("user");
        TourPackage tourPackage = tourPackageRepository.findById(id).orElse(null);
        model.addAttribute("pkg", tourPackage);
        model.addAttribute("loggedInUser", user);
        return "booking";
    }

    // 2. BOOK CUSTOM PACKAGE
    @GetMapping("/custom/{id}")
    public String showCustomBookingPage(@PathVariable int id, Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/user/login-page";
        }
        User user = (User) session.getAttribute("user");
        CustomPackage customPackage = customPackageService.getCustomPackageById(id);
        model.addAttribute("customPkg", customPackage);
        model.addAttribute("loggedInUser", user);
        return "booking-custom";
    }

    // 3. SAVE BOOKING
    @PostMapping("/save")
    public String saveBooking(@RequestParam String customerName, @RequestParam String customerEmail,
                              @RequestParam String bookingDate, @RequestParam int numberOfPeople,
                              @RequestParam String packageName, @RequestParam String location,
                              @RequestParam double totalPrice, @RequestParam(required = false, defaultValue = "0") int customPackageId,
                              Model model, HttpSession session) {
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
        model.addAttribute("customPackageId", customPackageId);
        return "booking-success";
    }

    // 4. ADMIN VIEW ALL BOOKINGS (Meeka thama list eka pennanna use wenne)
    @GetMapping("/list")
    public String showBookings(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        // Admin-ta okkoma bookings tika meken labenawa
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "booking-list";
    }

    // 5. ADMIN DELETE BOOKING
    @GetMapping("/delete/{id}")
    public String deleteBooking(@PathVariable int id, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        bookingService.deleteBooking(id);
        return "redirect:/admin/dashboard?tab=bookings";
    }

    // 6. USER SEE THEIR OWN BOOKINGS (Kalin hadapu filter logic eka meke thiyenawa)
    @GetMapping("/my-bookings")
    public String showMyBookingsPage(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/user/login-page";
        }
        User user = (User) session.getAttribute("user");

        // Methana 'getActiveBookingsByEmail' use karana nisa 'Cancelled' ewa user-ta penne ne
        List<Booking> bookings = bookingService.getActiveBookingsByEmail(user.getEmail());

        model.addAttribute("bookings", bookings);
        model.addAttribute("loggedInEmail", user.getEmail());
        return "my-bookings";
    }

    // 7. USER CANCELS BOOKING
    @GetMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable int id, HttpSession session,
                                org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        if (session.getAttribute("user") == null) {
            return "redirect:/user/login-page";
        }
        String message = bookingService.cancelBooking(id);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/booking/my-bookings";
    }
}