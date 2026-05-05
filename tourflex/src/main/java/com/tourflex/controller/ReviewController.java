package com.tourflex.controller;

import com.tourflex.model.Review;
import com.tourflex.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/add-page")
    public String showReviewPage(@RequestParam(required = false) String sort,
                                 @RequestParam(required = false) String pkg,
                                 Model model) {

        List<Review> reviews;

        if (pkg != null && !pkg.isEmpty()) {
            reviews = reviewService.getReviewsByPackage(pkg);
        } else if ("latest".equals(sort)) {
            reviews = reviewService.getLatestReviews();
        } else if ("oldest".equals(sort)) {
            reviews = reviewService.getOldestReviews();
        } else {
            reviews = reviewService.getAllReviews();
        }

        model.addAttribute("reviews", reviews);
        return "review";
    }

    @PostMapping("/save")
    public String saveReview(@RequestParam String customerName,
                             @RequestParam String customerEmail,
                             @RequestParam String packageName,
                             @RequestParam int rating,
                             @RequestParam String comment,
                             Model model) {

        Review review = new Review();
        review.setCustomerName(customerName);
        review.setCustomerEmail(customerEmail);
        review.setPackageName(packageName);
        review.setRating(rating);
        review.setComment(comment);
        review.setReviewDate(LocalDate.now().toString());

        reviewService.saveReview(review);

        return "redirect:/review/add-page";
    }

    @GetMapping("/delete/{id}")
    public String deleteReview(@PathVariable int id) {
        reviewService.deleteReview(id);
        return "redirect:/review/add-page";
    }

    @GetMapping("/edit/{id}")
    public String editReview(@PathVariable int id, Model model) {
        model.addAttribute("review", reviewService.getReviewById(id));
        return "edit-review";
    }

    @PostMapping("/update")
    public String updateReview(@ModelAttribute Review review) {
        reviewService.saveReview(review);
        return "redirect:/review/add-page";
    }
}