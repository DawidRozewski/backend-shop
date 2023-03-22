package com.example.shop.admin.order.controller.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class AdminOrderStats {
    private List<Integer> label;
    private List<BigDecimal> sale;
    private List<Long> order;
}
