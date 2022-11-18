package com.example.shop.admin.category.repository;

import com.example.shop.admin.category.model.AdminCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminCategoryRepository extends JpaRepository<AdminCategory, Long> {
}
