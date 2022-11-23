package com.example.shop.admin.review.repository;

import com.example.shop.admin.review.model.AdminReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AdminReviewRepository extends JpaRepository<AdminReview, Long> {

    @Modifying
    @Query("update AdminReview a set a.moderated = true where a.id = ?1")
    void moderate(Long id);

}
