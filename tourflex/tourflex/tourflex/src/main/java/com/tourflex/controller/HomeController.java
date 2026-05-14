package com.tourflex.controller;

import com.tourflex.model.TourPackage;
import com.tourflex.repository.TourPackageRepository;
import com.tourflex.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        // If not enough booked packages, show all available packages
        if (popularPackages.size() < 4) {
            List<TourPackage> allPackages = tourPackageRepository.findAll();
            for (TourPackage pkg : allPackages) {
                if (!popularPackages.contains(pkg) && popularPackages.size() < 4) {
                    popularPackages.add(pkg);
                }
            }
        }

        model.addAttribute("popularPackages", popularPackages);

        // Pass all unique locations for search autocomplete
        List<String> locations = tourPackageRepository.findAll().stream()
                .map(TourPackage::getLocation)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        model.addAttribute("allLocations", locations);

        return "home";
    }

    // REST API for live search suggestions
    @GetMapping("/api/search-packages")
    @ResponseBody
    public List<TourPackage> searchPackages(
            @RequestParam(required = false, defaultValue = "") String location,
            @RequestParam(required = false, defaultValue = "0") int guests) {

        List<TourPackage> all = tourPackageRepository.findAll();

        return all.stream()
                .filter(pkg -> location.isEmpty() ||
                        pkg.getLocation().toLowerCase().contains(location.toLowerCase()) ||
                        pkg.getName().toLowerCase().contains(location.toLowerCase()))
                .filter(pkg -> guests <= 0 || pkg.getMaxPeople() >= guests)
                .collect(Collectors.toList());
    }

    // REST API for location suggestions
    @GetMapping("/api/locations")
    @ResponseBody
    public List<String> getLocations(@RequestParam(required = false, defaultValue = "") String q) {
        return tourPackageRepository.findAll().stream()
                .map(TourPackage::getLocation)
                .distinct()
                .filter(loc -> q.isEmpty() || loc.toLowerCase().contains(q.toLowerCase()))
                .sorted()
                .collect(Collectors.toList());
    }
}