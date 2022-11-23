package com.example.shop.admin.review.controller;

import com.example.shop.admin.review.model.AdminReview;
import com.example.shop.admin.review.service.AdminReviewService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/reviews")
public class AdminReviewController {
    private final AdminReviewService reviewService;

    @GetMapping
    public List<AdminReview> getReviews() {
        return reviewService.getReviews();
    }

    @PutMapping("/{id}/moderate")
    public void moderate(@PathVariable Long id) {
        reviewService.moderate(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        reviewService.delete(id);
    }
}