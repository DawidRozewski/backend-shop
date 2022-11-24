package com.example.shop.cart.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private String currency;
    private String image;
    private String slug;

}
