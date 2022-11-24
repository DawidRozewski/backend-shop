package com.example.shop.cart.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class SummaryDTO {
    private BigDecimal grossValue;

}
