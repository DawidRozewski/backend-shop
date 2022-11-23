package com.example.shop.admin.review.service;

import com.example.shop.admin.review.model.AdminReview;
import com.example.shop.admin.review.repository.AdminReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class AdminReviewService {
    private final AdminReviewRepository reviewRepository;

    public List<AdminReview> getReviews() {
        return reviewRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Transactional
    public void moderate(Long id) {
        reviewRepository.moderate(id);
    }

    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }
}