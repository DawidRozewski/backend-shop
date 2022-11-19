package com.example.shop.review.controller.dto;

import org.hibernate.validator.constraints.Length;

public record ReviewDTO(@Length(min = 2, max = 60) String authorName,
                        @Length(min = 2, max = 600) String content,
                        Long productId) {
}
