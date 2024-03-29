package com.example.shop.admin.order.controller;

import com.example.shop.admin.order.model.dto.AdminOrderStats;
import com.example.shop.admin.order.service.AdminOrderStatsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/orders/stats")
public class AdminOrderStatsController {

    private final AdminOrderStatsService adminOrderStatsService;

    @GetMapping
    public AdminOrderStats getOrderStatistics() {
        return adminOrderStatsService.getStatistics();
    }
}
