package com.example.shop.admin.order.service;

import com.example.shop.admin.order.controller.model.AdminOrder;
import com.example.shop.admin.order.controller.model.AdminOrderStatus;
import com.example.shop.admin.order.repository.AdminOrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AdminExportService {

    private final AdminOrderRepository orderRepository;

    public List<AdminOrder> exportOrders(LocalDateTime from, LocalDateTime to, AdminOrderStatus orderStatus) {
        return orderRepository.findAllByPlaceDateIsBetweenAndOrderStatus(from, to, orderStatus);
    }
}
