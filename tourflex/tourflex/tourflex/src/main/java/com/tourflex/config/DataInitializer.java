package com.tourflex.config;

import com.tourflex.model.TourPackage;
import com.tourflex.repository.TourPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private TourPackageRepository tourPackageRepository;

    @Override
    public void run(String... args) {
        // Always ensure the 4 default packages exist with correct images
        if (tourPackageRepository.count() == 0) {
            System.out.println(">> Seeding default tour packages...");
            seedPackages();
        } else {
            // Update existing packages to have correct image names
            System.out.println(">> Updating package images...");
            for (TourPackage pkg : tourPackageRepository.findAll()) {
                switch (pkg.getLocation()) {
                    case "Ella":     pkg.setImageName("ella-pkg.jpeg"); break;
                    case "Yala":     pkg.setImageName("yala-pkg.jpeg"); break;
                    case "Bentota":  pkg.setImageName("bentota-pkg.jpeg"); break;
                    case "Sigiriya": pkg.setImageName("sigiriya-pkg.jpeg"); break;
                }
                tourPackageRepository.save(pkg);
            }
            System.out.println(">> Package images updated!");
        }
    }

    private void seedPackages() {
        tourPackageRepository.save(new TourPackage(
            "Ella Adventure Escape", "Adventure", "Single", 1, "Ella",
            2, 12500, "Hiking, zip line, train ride, nature observing",
            "ella-pkg.jpeg", null, null, null
        ));

        tourPackageRepository.save(new TourPackage(
            "Yala Safari Tour", "Safari", "Couple", 2, "Yala",
            3, 20000, "Jeep safari and hotel stay",
            "yala-pkg.jpeg", null, null, null
        ));

        tourPackageRepository.save(new TourPackage(
            "Bentota Luxury Stay", "Luxury", "Family", 5, "Bentota",
            4, 40000, "Beach resort and private transport",
            "bentota-pkg.jpeg", null, null, null
        ));

        tourPackageRepository.save(new TourPackage(
            "Sigiriya Cultural Tour", "Holiday Relax", "Group", 7, "Sigiriya",
            3, 60000, "Sigiriya rock climb, village tour, cultural experience and Hotel stay",
            "sigiriya-pkg.jpeg", null, null, null
        ));

        System.out.println(">> 4 default tour packages seeded successfully!");
    }
}
