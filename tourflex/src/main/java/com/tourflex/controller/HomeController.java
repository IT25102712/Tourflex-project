package com.tourflex.controller;

import com.tourflex.model.TourPackage;
import com.tourflex.repository.TourPackageRepository;
import com.tourflex.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private TourPackageRepository tourPackageRepository;

    @GetMapping("/")
    public String homePage(Model model) {
        List<String> topPackageNames = bookingService.getTopBookedPackageNames();
        List<TourPackage> popularPackages = new ArrayList<>();

        for (String packageName : topPackageNames) {
            TourPackage tourPackage = tourPackageRepository.findByName(packageName);
            if (tourPackage != null) {
                popularPackages.add(tourPackage);
            }
        }

        model.addAttribute("popularPackages", popularPackages);
        return "home";
    }
}