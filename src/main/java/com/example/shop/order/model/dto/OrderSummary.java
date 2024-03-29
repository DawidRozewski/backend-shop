package com.example.shop.order.model.dto;

import com.example.shop.common.model.OrderStatus;
import com.example.shop.order.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class OrderSummary {
    private Long id;
    private LocalDateTime placeDate;
    private OrderStatus status;
    private BigDecimal grossValue;
    private Payment payment;
    private String redirectUrl;

}
