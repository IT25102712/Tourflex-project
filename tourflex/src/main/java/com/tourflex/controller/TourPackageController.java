package com.tourflex.controller;

import com.tourflex.model.TourPackage;
import com.tourflex.service.TourPackageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/packages")
public class TourPackageController {

    @Autowired
    private TourPackageService tourPackageService;

    private final String uploadDir = "uploads";

    @PostMapping("/add")
    public String addPackage(@RequestParam String name,
                             @RequestParam String category,
                             @RequestParam String type,
                             @RequestParam int maxPeople,
                             @RequestParam String location,
                             @RequestParam int days,
                             @RequestParam double price,
                             @RequestParam String description,
                             @RequestParam String hotelName,
                             @RequestParam String transportMethods,
                             @RequestParam String meals,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             HttpSession session) throws IOException {

        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        TourPackage tourPackage = new TourPackage();
        tourPackage.setName(name);
        tourPackage.setCategory(category);
        tourPackage.setType(type);
        tourPackage.setMaxPeople(maxPeople);
        tourPackage.setLocation(location);
        tourPackage.setDays(days);
        tourPackage.setPrice(price);
        tourPackage.setDescription(description);
        tourPackage.setHotelName(hotelName);
        tourPackage.setTransportMethods(transportMethods);
        tourPackage.setMeals(meals);

        if (!imageFile.isEmpty()) {
            Files.createDirectories(Paths.get(uploadDir));
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, imageFile.getBytes());
            tourPackage.setImageName(fileName);
        }

        tourPackageService.savePackageOnly(tourPackage);
        return "redirect:/packages/manage";
    }

    @GetMapping("/add-page")
    public String showAddPackagePage(HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        return "add-package";
    }

    @GetMapping("/list")
    public String showAllPackages(Model model) {
        model.addAttribute("packages", tourPackageService.getAllPackages());
        return "package-list";
    }

    @GetMapping("/details/{id}")
    public String showPackageDetails(@PathVariable int id, Model model) {
        TourPackage tourPackage = tourPackageService.getPackageById(id);
        model.addAttribute("pkg", tourPackage);
        return "package-details";
    }

    @GetMapping("/manage")
    public String managePackages(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("packages", tourPackageService.getAllPackages());
        return "manage-packages";
    }

    @GetMapping("/delete/{id}")
    public String deletePackage(@PathVariable int id, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        tourPackageService.deletePackage(id);
        return "redirect:/packages/manage";
    }

    @GetMapping("/edit/{id}")
    public String showEditPackagePage(@PathVariable int id, Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        TourPackage tourPackage = tourPackageService.getPackageById(id);
        model.addAttribute("pkg", tourPackage);
        return "edit-package";
    }

    @PostMapping("/update")
    public String updatePackage(@RequestParam int id,
                                @RequestParam String name,
                                @RequestParam String category,
                                @RequestParam String type,
                                @RequestParam int maxPeople,
                                @RequestParam String location,
                                @RequestParam int days,
                                @RequestParam double price,
                                @RequestParam String description,
                                @RequestParam String hotelName,
                                @RequestParam String transportMethods,
                                @RequestParam String meals,
                                @RequestParam("imageFile") MultipartFile imageFile,
                                HttpSession session) throws IOException {

        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        TourPackage existingPackage = tourPackageService.getPackageById(id);

        if (existingPackage == null) {
            return "redirect:/packages/manage";
        }

        existingPackage.setName(name);
        existingPackage.setCategory(category);
        existingPackage.setType(type);
        existingPackage.setMaxPeople(maxPeople);
        existingPackage.setLocation(location);
        existingPackage.setDays(days);
        existingPackage.setPrice(price);
        existingPackage.setDescription(description);
        existingPackage.setHotelName(hotelName);
        existingPackage.setTransportMethods(transportMethods);
        existingPackage.setMeals(meals);

        if (!imageFile.isEmpty()) {
            Files.createDirectories(Paths.get(uploadDir));
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, imageFile.getBytes());
            existingPackage.setImageName(fileName);
        }

        tourPackageService.savePackageOnly(existingPackage);
        return "redirect:/packages/manage";
    }
}