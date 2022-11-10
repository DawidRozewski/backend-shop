package com.example.shop.admin.product.controller;

import com.example.shop.admin.product.model.AdminProduct;
import com.example.shop.admin.product.service.AdminProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;

    @GetMapping("/admin/products")
    public Page<AdminProduct> getProducts(Pageable pageble) {
        return adminProductService.getProducts(pageble);
    }
}
