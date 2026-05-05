package com.tourflex.controller;

import com.tourflex.model.CustomPackage;
import com.tourflex.model.User;
import com.tourflex.service.CustomPackageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/custom-package")
public class CustomPackageController {

    @Autowired
    private CustomPackageService customPackageService;

    // CREATE PAGE
    @GetMapping("/create")
    public String showCreatePage(Model model) {
        model.addAttribute("pkg", new CustomPackage());
        return "custom-package";
    }

    // SAVE NEW
    @PostMapping("/save")
    public String savePackage(@ModelAttribute CustomPackage pkg, HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/user/login-page";
        }

        User user = (User) session.getAttribute("user");
        pkg.setCustomerEmail(user.getEmail());

        double price = pkg.getNumberOfDays() * 5000
                + pkg.getNumberOfPeople() * 2000;

        pkg.setTotalPrice(price);

        customPackageService.savePackage(pkg);

        return "redirect:/custom-package/my";
    }

    // VIEW MY PACKAGES
    @GetMapping("/my")
    public String myPackages(Model model, HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/user/login-page";
        }

        User user = (User) session.getAttribute("user");

        List<CustomPackage> packages =
                customPackageService.getPackagesByCustomerEmail(user.getEmail());

        model.addAttribute("packages", packages);

        return "my-custom-packages";
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public String deletePackage(@PathVariable int id) {
        customPackageService.deletePackage(id);
        return "redirect:/custom-package/my";
    }

    // EDIT PAGE
    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable int id, Model model, HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/user/login-page";
        }

        CustomPackage pkg = customPackageService.getCustomPackageById(id);

        model.addAttribute("pkg", pkg);

        return "custom-package-edit";
    }

    // UPDATE
    @PostMapping("/update")
    public String updatePackage(@ModelAttribute CustomPackage pkg, HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/user/login-page";
        }

        double price = pkg.getNumberOfDays() * 5000
                + pkg.getNumberOfPeople() * 2000;

        pkg.setTotalPrice(price);

        customPackageService.savePackage(pkg);

        return "redirect:/custom-package/my";
    }
}