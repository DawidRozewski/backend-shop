package com.example.shop.admin.order.service;

import com.example.shop.admin.order.model.AdminOrder;
import com.example.shop.admin.order.model.dto.AdminOrderStats;
import com.example.shop.admin.order.repository.AdminOrderRepository;
import com.example.shop.common.model.OrderStatus;
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
                OrderStatus.COMPLETED
        );

        TreeMap<Integer, AdminOrdersStatsValue> result = new TreeMap<>();
        for (int i = from.getDayOfMonth(); i < to.getDayOfMonth(); i++) {
            result.put(i, aggregateValues(i, orders));
        }

        List<Long> ordersList = result.values().stream()
                .map(v -> v.orders()).toList();
        List<BigDecimal> salesList = result.values().stream()
                .map(v -> v.sales()).toList();

        return AdminOrderStats.builder()
                .label(result.keySet().stream().toList())
                .sale(result.values().stream().map(o -> o.sales).toList())
                .order(result.values().stream().map(o -> o.orders).toList())
                .ordersCount(ordersList.stream().reduce(Long::sum).orElse(0L))
                .salesSum(salesList.stream().reduce(BigDecimal::add).orElse(BigDecimal.ZERO))
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
