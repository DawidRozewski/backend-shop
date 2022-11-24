package com.example.shop.cart.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder

public class CartSummaryDTO {
    private Long id;
    private List<CartSummaryItemDTO> items;
    private SummaryDTO summary;
}
