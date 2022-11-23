package com.example.shop.product.controller;

import com.example.shop.common.dto.ProductListDTO;
import com.example.shop.common.model.Product;
import com.example.shop.product.service.ProductService;
import com.example.shop.product.service.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;

    @GetMapping(value = "/products")
    public Page<ProductListDTO> getProducts(Pageable pageable) {
        Page<Product> products = productService.getProducts(pageable);
        List<ProductListDTO> productListDTOS = products.getContent().stream()
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
        return new PageImpl<>(productListDTOS, pageable, products.getTotalElements());
    }

    @GetMapping(value = "/products/{slug}")
    public ProductDTO getProductBySlug(
            @PathVariable
            @Pattern(regexp = "[a-z0-9\\-]+")
            @Length(max = 255)
            String slug) {
        return productService.getProductBySlug(slug);
    }

}
