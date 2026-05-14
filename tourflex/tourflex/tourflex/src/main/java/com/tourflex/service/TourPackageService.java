package com.tourflex.service;

import com.tourflex.model.TourPackage;
import com.tourflex.repository.TourPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourPackageService {

    @Autowired
    private TourPackageRepository tourPackageRepository;

    public String savePackage(TourPackage tourPackage) {
        tourPackageRepository.save(tourPackage);
        return "Package added successfully!";
    }

    public TourPackage savePackageOnly(TourPackage tourPackage) {
        return tourPackageRepository.save(tourPackage);
    }

    public List<TourPackage> getAllPackages() {
        return tourPackageRepository.findAll();
    }

    public void deletePackage(int id) {
        tourPackageRepository.deleteById(id);
    }

    public TourPackage getPackageById(int id) {
        return tourPackageRepository.findById(id).orElse(null);
    }
}