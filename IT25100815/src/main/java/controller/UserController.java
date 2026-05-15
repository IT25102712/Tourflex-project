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

    // reg page
    @GetMapping("/register-page")
    public String showRegisterPage() {
        return "register";
    }

    // login
    @GetMapping("/login-page")
    public String showLoginPage() {
        return "login";
    }

    // reg
    @PostMapping("/register")
    public String registerUser(@RequestParam String name,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam String phone,
                               @RequestParam String address,
                               Model model) {

        // @gmail.com check
        if (!email.toLowerCase().endsWith("@gmail.com")) {
            model.addAttribute("error", "Invalid email address");
            return "register";
        }

        // phone validation 10 digit start from 0
        if (phone.length() != 10 || !phone.matches("^0\\d{9}$")) {
            model.addAttribute("error", "Phone number must be exactly 10 digits. Start with 0");
            return "register";
        }

        // Password validation
        // 1 lett, 1 num, 1 sym, and min 6 chars
        String passRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&]).{6,}$";
        if (!password.matches(passRegex)) {
            model.addAttribute("error", "Password must be 6+ characters with a mixture of letters, numbers, and symbols.");
            return "register";
        }

        if (userService.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Email is already registered!");
            return "register"; // return reg page
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

    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            model.addAttribute("error", "Fields cannot be empty");
            return "login";
        }

        // login credential check
        User user = userService.login(email, password);
        if(user != null){
            session.setAttribute("user", user);
            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }

    }

    // user profile
    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/user/login-page"; // Redirect if not logged in
        }

        // update latest data after refreshing the page
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

        // booking in user
        List<Booking> userBookings = bookingService.getBookingsByEmail(userToUse.getEmail());
        model.addAttribute("userBookings", userBookings);

        // spent amount
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
        // login check
        User user = (User) session.getAttribute("user");

        if (user != null) {
            model.addAttribute("user", user);
            return "edit-user";
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
                    .contentType(MediaType.IMAGE_JPEG) // or png image
                    .body(user.get().getImage());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser != null) {
            // image set to user
            user.setImage(sessionUser.getImage());
        }
        userService.updateUser(user);
        session.setAttribute("user", user); // Update
        return "redirect:/user/profile";
    }

    @PostMapping("/delete")
    public String deleteUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            userService.deleteUser(user.getId());
            session.invalidate();
        }
        return "redirect:/"; // back to home page
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/";
    }
}