package com.tourflex.service;

import com.tourflex.model.Review;
import com.tourflex.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getLatestReviews() {
        return reviewRepository.findAllByOrderByReviewDateDesc();
    }

    public List<Review> getOldestReviews() {
        return reviewRepository.findAllByOrderByReviewDateAsc();
    }

    public List<Review> searchReviews(String keyword, String sort) {
        if ("oldest".equals(sort)) {
            return reviewRepository.findByPackageNameContainingIgnoreCaseOrCustomerNameContainingIgnoreCaseOrderByReviewDateAsc(keyword, keyword);
        }
        return reviewRepository.findByPackageNameContainingIgnoreCaseOrCustomerNameContainingIgnoreCaseOrderByReviewDateDesc(keyword, keyword);
    }

    public void deleteReview(int id) {
        reviewRepository.deleteById(id);
    }

    public Review getReviewById(int id) {
        return reviewRepository.findById(id).orElse(null);
    }
}