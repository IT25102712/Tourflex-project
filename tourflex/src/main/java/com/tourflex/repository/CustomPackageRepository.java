package com.tourflex.repository;

import com.tourflex.model.CustomPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomPackageRepository extends JpaRepository<CustomPackage, Integer> {

    List<CustomPackage> findByCustomerEmail(String customerEmail);

}