package com.example.shop.admin.order.service;

import com.example.shop.admin.order.model.AdminOrder;
import com.example.shop.admin.order.repository.AdminOrderRepository;
import com.example.shop.common.model.OrderStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AdminExportService {

    private final AdminOrderRepository orderRepository;

    public List<AdminOrder> exportOrders(LocalDateTime from, LocalDateTime to, OrderStatus orderStatus) {
        return orderRepository.findAllByPlaceDateIsBetweenAndOrderStatus(from, to, orderStatus);
    }
}
