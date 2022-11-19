package com.example.shop.category.model;

import com.example.shop.product.controller.dto.ProductListDTO;
import org.springframework.data.domain.Page;

public record CategoryProductsDTO(Category category, Page<ProductListDTO> products) {
}
