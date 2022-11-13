package com.example.shop.admin.product.controller;

import com.example.shop.admin.product.controller.dto.AdminProductDTO;
import com.example.shop.admin.product.model.AdminProduct;
import com.example.shop.admin.product.service.AdminProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;

@RestController
@AllArgsConstructor
public class AdminProductController {

    public static final Long EMPTY_ID = null;
    private final AdminProductService productService;

    @GetMapping("/admin/products")
    public Page<AdminProduct> getProducts(Pageable pageable) {
        return productService.getProducts(pageable);
    }

    @GetMapping("/admin/products/{id}")
    public AdminProduct getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @PostMapping("/admin/products")
    public AdminProduct createProduct(@RequestBody @Valid AdminProductDTO adminProductDTO) {
        return productService.createProduct(mapAdminProduct(adminProductDTO, EMPTY_ID));
    }

    @PutMapping("/admin/products/{id}")
    public AdminProduct updateProduct(@RequestBody @Valid AdminProductDTO adminProductDTO,
                                      @PathVariable Long id) {
        return productService.updateProduct(mapAdminProduct(adminProductDTO, id)
        );
    }

    private static AdminProduct mapAdminProduct(AdminProductDTO adminProductDTO, Long id) {
        return AdminProduct.builder()
                .id(id)
                .name(adminProductDTO.getName())
                .description(adminProductDTO.getDescription())
                .category(adminProductDTO.getCategory())
                .price(adminProductDTO.getPrice())
                .currency(adminProductDTO.getCurrency().toUpperCase(Locale.ROOT))
                .build();
    }


}
