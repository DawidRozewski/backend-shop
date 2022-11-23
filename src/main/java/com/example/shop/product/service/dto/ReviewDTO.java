package com.example.shop.product.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewDTO {
    private Long id;
    private Long productId;
    private String authorName;
    private String content;
    private boolean moderate;

}
