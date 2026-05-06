package com.tourflex.controller;

import com.tourflex.model.User;
import com.tourflex.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
                               Model model) {

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

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

        User user = userService.login(email, password);

        if(user != null){
            session.setAttribute("user", user);
            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    // LOGOUT
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
}