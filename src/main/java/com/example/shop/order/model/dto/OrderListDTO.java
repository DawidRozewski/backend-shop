package com.example.shop.order.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderListDTO {
    private Long id;
    private LocalDateTime placeDate;
    private String orderStatus;
    private BigDecimal grossValue;
}
