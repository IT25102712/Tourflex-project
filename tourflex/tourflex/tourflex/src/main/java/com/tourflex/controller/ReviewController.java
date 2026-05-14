package com.tourflex.controller;

import com.tourflex.model.Review;
import com.tourflex.model.User;
import com.tourflex.service.ReviewService;
import com.tourflex.service.TourPackageService;
import jakarta.servlet.http.HttpSession;
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

    @Autowired
    private TourPackageService tourPackageService;

    @GetMapping("/add-page")
    public String showReviewPage(@RequestParam(required = false) String sort,
                                 @RequestParam(required = false) String search,
                                 HttpSession session,
                                 Model model) {

        List<Review> reviews;

        if (search != null && !search.trim().isEmpty()) {
            reviews = reviewService.searchReviews(search.trim(), sort);
        } else if ("latest".equals(sort)) {
            reviews = reviewService.getLatestReviews();
        } else if ("oldest".equals(sort)) {
            reviews = reviewService.getOldestReviews();
        } else {
            // Default sort if nothing selected
            reviews = reviewService.getLatestReviews();
        }

        model.addAttribute("reviews", reviews);
        model.addAttribute("packages", tourPackageService.getAllPackages());
        model.addAttribute("currentSort", sort);
        model.addAttribute("currentSearch", search);
        return "review";
    }

    @PostMapping("/save")
    public String saveReview(@RequestParam String packageName,
                             @RequestParam int rating,
                             @RequestParam String comment,
                             HttpSession session,
                             Model model) {

        if (session.getAttribute("user") == null) {
            return "redirect:/user/login-page";
        }

        User user = (User) session.getAttribute("user");

        Review review = new Review();
        review.setCustomerName(user.getName());
        review.setCustomerEmail(user.getEmail());
        review.setPackageName(packageName);
        review.setRating(rating);
        review.setComment(comment);
        review.setReviewDate(LocalDate.now().toString());

        reviewService.saveReview(review);

        return "redirect:/review/add-page";
    }

    // Only allow deletion by the review owner or admin
    @GetMapping("/delete/{id}")
    public String deleteReview(@PathVariable int id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Boolean isAdmin = (Boolean) session.getAttribute("admin");

        Review review = reviewService.getReviewById(id);
        if (review != null) {
            if (isAdmin != null || (user != null && user.getEmail().equals(review.getCustomerEmail()))) {
                reviewService.deleteReview(id);
            }
        }
        return "redirect:/review/add-page";
    }

    // Only allow editing by the review owner
    @GetMapping("/edit/{id}")
    public String editReview(@PathVariable int id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        Review review = reviewService.getReviewById(id);

        if (review == null) {
            return "redirect:/review/add-page";
        }
        if (user == null || !user.getEmail().equals(review.getCustomerEmail())) {
            return "redirect:/review/add-page";
        }

        model.addAttribute("review", review);
        return "edit-review";
    }

    @PostMapping("/update")
    public String updateReview(@ModelAttribute Review review, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login-page";
        }
        review.setCustomerName(user.getName());
        review.setCustomerEmail(user.getEmail());
        review.setReviewDate(LocalDate.now().toString());
        reviewService.saveReview(review);
        return "redirect:/review/add-page";
    }
}