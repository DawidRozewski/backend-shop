package com.example.shop.category.model;

import com.example.shop.product.model.Product;
import org.springframework.data.domain.Page;

public record CategoryProductsDTO(Category category, Page<Product> products) {
}
