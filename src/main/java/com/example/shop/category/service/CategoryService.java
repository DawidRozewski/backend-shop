package com.example.shop.category.service;

import com.example.shop.category.controller.dto.CategoryProductsDTO;
import com.example.shop.category.repository.CategoryRepository;
import com.example.shop.common.dto.ProductListDTO;
import com.example.shop.common.model.Category;
import com.example.shop.common.model.Product;
import com.example.shop.common.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CategoryProductsDTO getCategoriesWithProducts(String slug, Pageable pageable) {
        Category category = categoryRepository.findBySlug(slug);
        Page<Product> page = productRepository.findByCategoryId(category.getId(), pageable);
        List<ProductListDTO> productListDTOS = page.getContent().stream()
                .map(product -> ProductListDTO.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .currency(product.getCurrency())
                        .image(product.getImage())
                        .slug(product.getSlug())
                        .build()
                ).toList();

        return new CategoryProductsDTO(category, new PageImpl<>(productListDTOS, pageable, page.getTotalElements()));
    }
}
