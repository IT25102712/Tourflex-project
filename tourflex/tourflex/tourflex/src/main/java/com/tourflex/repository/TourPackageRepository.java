package com.tourflex.repository;

import com.tourflex.model.TourPackage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourPackageRepository extends JpaRepository<TourPackage, Integer> {
    TourPackage findByName(String name);
}