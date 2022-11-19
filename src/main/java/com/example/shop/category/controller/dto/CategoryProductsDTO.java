package com.example.shop.category.controller.dto;

import com.example.shop.common.dto.ProductListDTO;
import com.example.shop.common.model.Category;
import org.springframework.data.domain.Page;

public record CategoryProductsDTO(Category category, Page<ProductListDTO> products) {
}
