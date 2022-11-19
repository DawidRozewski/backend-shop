package com.example.shop.review.controller;

import com.example.shop.review.controller.dto.ReviewDTO;
import com.example.shop.review.model.Review;
import com.example.shop.review.service.ReviewService;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/reviews")
    public Review addReview(@RequestBody @Valid ReviewDTO reviewDTO) {
        return reviewService.addReview(Review.builder()
                .authorName(cleanContent(reviewDTO.authorName()))
                .content(cleanContent(reviewDTO.content()))
                .productId(reviewDTO.productId())
                .build());
    }

    private String cleanContent(String text) {
        return Jsoup.clean(text, Safelist.none());
    }
}
