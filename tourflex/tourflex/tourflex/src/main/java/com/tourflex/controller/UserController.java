package com.tourflex.controller;

import com.tourflex.model.Booking;
import com.tourflex.model.SavedCard;
import com.tourflex.model.User;
import com.tourflex.service.BookingService;
import com.tourflex.service.SavedCardService;
import com.tourflex.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SavedCardService savedCardService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private com.tourflex.service.ReviewService reviewService;

    // REGISTER PAGE
    @GetMapping("/register-page")
    public String showRegisterPage() {
        return "register";
    }

    // LOGIN PAGE
    @GetMapping("/login-page")
    public String showLoginPage() {
        return "login";
    }

    // REGISTER USER
    @PostMapping("/register")
    public String registerUser(@RequestParam String name,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam String phone,
                               @RequestParam String address,
                               Model model) {

        // Email Validation @gmail.com
        if (!email.toLowerCase().endsWith("@gmail.com")) {
            model.addAttribute("error", "Invalid email address");
            return "register";
        }

        // Phone Validation (Must be exactly 10 digits, start with 0)
        if (phone.length() != 10 || !phone.matches("^0\\d{9}$")) {
            model.addAttribute("error", "Phone number must be exactly 10 digits. Start with 0");
            return "register";
        }

        // Password validation (Letters + Numbers + Symbols)
        // requires: 1 Letter, 1 Number, 1 Symbol, and min 6 chars
        String passRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&]).{6,}$";
        if (!password.matches(passRegex)) {
            model.addAttribute("error", "Password must be 6+ characters with a mixture of letters, numbers, and symbols.");
            return "register";
        }

        if (userService.findByEmail(email).isPresent()) {
            // 2. If it exists, send an error message to the UI
            model.addAttribute("error", "Email is already registered!");
            return "register"; // Return back to the registration page
        }

        // Save user
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);
        user.setAddress(address);

        try {
            userService.register(user);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            model.addAttribute("error", "An account with this email or phone number already exists.");
            return "register";
        }

        model.addAttribute("message", "Registration successful! Please login.");
        return "login";
    }

    // USER LOGIN - handles only regular users
    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            model.addAttribute("error", "Fields cannot be empty");
            return "login";
        }

        // Regular user login only
        User user = userService.login(email, password);
        if(user != null){
            session.setAttribute("user", user);
            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }

    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/user/login-page"; // Redirect if not logged in
        }
        // Refresh user from DB to get latest data
        Optional<User> freshUser = userService.findById(loggedInUser.getId());
        User userToUse;
        if (freshUser.isPresent()) {
            session.setAttribute("user", freshUser.get());
            userToUse = freshUser.get();
        } else {
            userToUse = loggedInUser;
        }
        
        model.addAttribute("user", userToUse);
        model.addAttribute("savedCards", savedCardService.getCardsByEmail(userToUse.getEmail()));

        // User's bookings for profile view
        List<Booking> userBookings = bookingService.getBookingsByEmail(userToUse.getEmail());
        model.addAttribute("userBookings", userBookings);

        // Calculate total spent and trips based on payment status
        long paidTrips = userBookings.stream()
                .filter(b -> "Paid".equals(b.getBookingStatus()) || "Refund Requested".equals(b.getBookingStatus()))
                .count();
                
        double totalSpent = userBookings.stream()
                .filter(b -> "Paid".equals(b.getBookingStatus()) || "Refund Requested".equals(b.getBookingStatus()))
                .mapToDouble(Booking::getTotalPrice)
                .sum();
                
        model.addAttribute("totalSpent", totalSpent);
        model.addAttribute("totalTrips", paidTrips);
        
        List<com.tourflex.model.Review> userReviews = reviewService.getAllReviews().stream()
                .filter(r -> r.getCustomerEmail().equals(userToUse.getEmail()))
                .toList();
        model.addAttribute("userReviews", userReviews);
        
        return "profile"; //html
    }

    @GetMapping("/edit")
    public String showEditForm(HttpSession session, Model model) {
        // Get the logged-in user from the session
        User user = (User) session.getAttribute("user");

        if (user != null) {
            model.addAttribute("user", user);
            return "edit-user"; // Make sure this matches your HTML filename
        } else {
            return "redirect:/user/login-page";
        }
    }

    @PostMapping("/upload-image")
    public String uploadImage(@RequestParam("profileImage") MultipartFile file, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!file.isEmpty() && user != null) {
            try {
                user.setImage(file.getBytes());
                userService.updateUser(user); // Save to DB
                session.setAttribute("user", user); // Update Session
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/user/profile";
    }

    @GetMapping("/display/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> displayImage(@PathVariable int id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent() && user.get().getImage() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Or IMAGE_PNG
                    .body(user.get().getImage());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser != null) {
            // Preserve the image from session since form doesn't send it
            user.setImage(sessionUser.getImage());
        }
        userService.updateUser(user);
        session.setAttribute("user", user); // Update
        return "redirect:/user/profile";
    }

    // FIXED: Changed from GET to POST for delete operation
    @PostMapping("/delete")
    public String deleteUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            userService.deleteUser(user.getId());
            session.invalidate();
        }
        return "redirect:/"; // back to home page
    }

    // LOGOUT
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/";
    }
}