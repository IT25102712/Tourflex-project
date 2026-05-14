package com.tourflex.service;

import com.tourflex.model.CustomPackage;
import com.tourflex.repository.CustomPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomPackageService {

    @Autowired
    private CustomPackageRepository customPackageRepository;

    public CustomPackage savePackage(CustomPackage pkg) {
        return customPackageRepository.save(pkg);
    }

    public List<CustomPackage> getPackagesByCustomerEmail(String email) {
        return customPackageRepository.findByCustomerEmail(email);
    }

    public void deletePackage(int id) {
        customPackageRepository.deleteById(id);
    }

    // 🔥 IMPORTANT METHOD (used in controller)
    public CustomPackage getCustomPackageById(int id) {
        return customPackageRepository.findById(id).orElse(null);
    }

    // Mark package as Paid after successful payment
    public void updatePaymentStatus(int id, String status) {
        CustomPackage pkg = customPackageRepository.findById(id).orElse(null);
        if (pkg != null) {
            pkg.setPaymentStatus(status);
            customPackageRepository.save(pkg);
        }
    }
}