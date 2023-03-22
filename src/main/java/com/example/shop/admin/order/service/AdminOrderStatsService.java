package com.example.shop.admin.order.service;

import com.example.shop.admin.order.controller.model.AdminOrder;
import com.example.shop.admin.order.controller.model.AdminOrderStatus;
import com.example.shop.admin.order.controller.model.dto.AdminOrderStats;
import com.example.shop.admin.order.repository.AdminOrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeMap;

@Service
@AllArgsConstructor
public class AdminOrderStatsService {

    private final AdminOrderRepository orderRepository;

    public AdminOrderStats getStatistics() {
        LocalDateTime from = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime to = LocalDateTime.now();
        List<AdminOrder> orders = orderRepository.findAllByPlaceDateIsBetweenAndOrderStatus(
                from,
                to,
                AdminOrderStatus.COMPLETED
        );

        TreeMap<Integer, AdminOrdersStatsValue> result = new TreeMap<>();
        for (int i = from.getDayOfMonth(); i < to.getDayOfMonth(); i++) {
            result.put(i, aggregateValues(i, orders));
        }
        return AdminOrderStats.builder()
                .label(result.keySet().stream().toList())
                .sale(result.values().stream().map(o -> o.sales).toList())
                .order(result.values().stream().map(o -> o.orders).toList())
                .build();
    }

    private AdminOrdersStatsValue aggregateValues(int i, List<AdminOrder> orders) {
        BigDecimal totalValue = BigDecimal.ZERO;
        Long orderCount = 0L;
        for (AdminOrder order : orders) {
            if (i == order.getPlaceDate().getDayOfMonth()) {
                totalValue = totalValue.add(order.getGrossValue());
                orderCount++;
            }
        }
        return new AdminOrdersStatsValue(totalValue, orderCount);
    }

    private record AdminOrdersStatsValue(BigDecimal sales, Long orders) {
    }
}
