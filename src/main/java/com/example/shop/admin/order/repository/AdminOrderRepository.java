package com.example.shop.admin.order.repository;

import com.example.shop.admin.order.model.AdminOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminOrderRepository extends JpaRepository<AdminOrder, Long> {
}
