package com.tourflex.repository;

import com.tourflex.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findAllByOrderByReviewDateDesc();

    List<Review> findAllByOrderByReviewDateAsc();

    // 🔥 FIXED (contains instead of exact match)
    List<Review> findByPackageNameContainingIgnoreCaseOrderByReviewDateDesc(String packageName);
}