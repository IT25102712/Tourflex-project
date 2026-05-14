package com.tourflex.controller;

import com.tourflex.model.SavedCard;
import com.tourflex.model.User;
import com.tourflex.service.SavedCardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/saved-card")
public class SavedCardController {

    @Autowired
    private SavedCardService savedCardService;

    @PostMapping("/save")
    public String saveCard(@RequestParam String cardHolderName,
                           @RequestParam String cardNumber,
                           @RequestParam String expiryDate,
                           @RequestParam String cardType,
                           @RequestParam(required = false, defaultValue = "0") double amount,
                           @RequestParam(required = false, defaultValue = "0") int customPackageId,
                           HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/user/login-page";
        }

        User user = (User) session.getAttribute("user");

        SavedCard savedCard = new SavedCard();
        savedCard.setCustomerEmail(user.getEmail());
        savedCard.setCardHolderName(cardHolderName);
        savedCard.setCardNumber(cardNumber);
        savedCard.setExpiryDate(expiryDate);
        savedCard.setCardType(cardType);

        savedCardService.saveCard(savedCard);

        return "redirect:/payment/page?amount=" + amount + "&customPackageId=" + customPackageId;
    }

    @GetMapping("/delete/{id}")
    public String deleteCard(@PathVariable int id,
                             @RequestParam(required = false, defaultValue = "0") double amount,
                             @RequestParam(required = false, defaultValue = "0") int customPackageId,
                             @RequestParam(required = false, defaultValue = "payment") String from,
                             HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/user/login-page";
        }

        savedCardService.deleteCard(id);

        // If deletion is from profile, redirect back to profile
        if ("profile".equals(from)) {
            return "redirect:/user/profile";
        }

        return "redirect:/payment/page?amount=" + amount + "&customPackageId=" + customPackageId;
    }
}