package com.example.shop.admin.order.repository;

import com.example.shop.admin.order.controller.model.AdminOrderLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminOrderLogRepository extends JpaRepository<AdminOrderLog, Long> {
}
