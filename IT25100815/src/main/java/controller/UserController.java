package com.tourflex.controller;

import com.tourflex.model.User;
import com.tourflex.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

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

        // Phone Validation (Must be exactly 10 digits)
        if (phone.length() != 10 || !phone.matches("\\d+")) {
            model.addAttribute("error", "Phone number must be exactly 10 digits.Start with 0");
            return "register";
        }

        // Password validation (Letters + Numbers + Symbols)
        // requires: 1 Letter, 1 Number, 1 Symbol, and min 6 chars
        String passRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{6,}$";
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

        userService.register(user);

        model.addAttribute("message", "Registration successful! Please login.");
        return "login";
    }

    // LOGIN USER
    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            model.addAttribute("error", "Fields cannot be empty");
            return "login";
        }

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
        model.addAttribute("user", loggedInUser);
        return "profile"; //html
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Optional<User> user = userService.findById(id); // findById in Service
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "edit-user"; //
        } else {
            return "redirect:/user/profile"; // Redirect if ID is wrong
        }
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user, HttpSession session) {
        userService.updateUser(user);
        session.setAttribute("user", user); // Update
        return "redirect:/user/profile";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id, HttpSession session) {
        userService.deleteUser(id);
        session.invalidate();
        return "redirect:/"; // back to home page
    }

    // LOGOUT
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
}