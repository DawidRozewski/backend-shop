package com.example.shop.order.repository;

import com.example.shop.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);

    Optional<Order> findByOrderHash(String orderHash);
}
