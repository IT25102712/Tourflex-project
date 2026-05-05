package com.tourflex.controller;

import com.tourflex.model.Payment;
import com.tourflex.model.SavedCard;
import com.tourflex.model.User;
import com.tourflex.service.PaymentService;
import com.tourflex.service.SavedCardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private SavedCardService savedCardService;

    @GetMapping("/page")
    public String showPaymentPage(@RequestParam(required = false, defaultValue = "0") double amount,
                                  HttpSession session,
                                  Model model) {

        if (session.getAttribute("user") == null) {
            return "redirect:/user/login-page";
        }

        User user = (User) session.getAttribute("user");
        List<SavedCard> savedCards = savedCardService.getCardsByEmail(user.getEmail());

        model.addAttribute("savedCards", savedCards);
        model.addAttribute("loggedInUser", user);
        model.addAttribute("amount", amount);

        return "payment";
    }

    @PostMapping("/save")
    public String savePayment(@RequestParam String customerName,
                              @RequestParam String customerEmail,
                              @RequestParam String cardHolderName,
                              @RequestParam String cardNumber,
                              @RequestParam String expiryDate,
                              @RequestParam String cvv,
                              @RequestParam double amount,
                              Model model,
                              HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/user/login-page";
        }

        Payment payment = new Payment();
        payment.setCustomerName(customerName);
        payment.setCustomerEmail(customerEmail);
        payment.setCardHolderName(cardHolderName);
        payment.setCardNumber(cardNumber);
        payment.setExpiryDate(expiryDate);
        payment.setCvv(cvv);
        payment.setAmount(amount);
        payment.setPaymentStatus("Paid");

        paymentService.savePayment(payment);

        model.addAttribute("payment", payment);
        model.addAttribute("message", "Payment successful!");
        return "payment-success";
    }

    @GetMapping("/list")
    public String showPayments(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("payments", paymentService.getAllPayments());
        return "payment-list";
    }
}