package com.example.shop.product.controller;

import com.example.shop.product.model.Product;
import com.example.shop.product.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    @GetMapping(value = "/products")
    public Page<Product> getProducts(@PageableDefault(size = 25) Pageable pageable) {
        return productService.getProducts(pageable);
    }

}
