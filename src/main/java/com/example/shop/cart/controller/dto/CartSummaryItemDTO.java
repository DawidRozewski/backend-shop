package com.example.shop.cart.controller.dto;


import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CartSummaryItemDTO {
    private Long id;
    private int quantity;
    private ProductDTO product;
    private BigDecimal lineValue;

}



