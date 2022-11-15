package com.example.shop.admin.product.controller;

import com.example.shop.admin.product.controller.dto.AdminProductDTO;
import com.example.shop.admin.product.controller.dto.UploadResponse;
import com.example.shop.admin.product.model.AdminProduct;
import com.example.shop.admin.product.service.AdminProductImageService;
import com.example.shop.admin.product.service.AdminProductService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@AllArgsConstructor
public class AdminProductController {

    public static final Long EMPTY_ID = null;
    private final AdminProductService productService;
    private final AdminProductImageService productImageService;

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

    @DeleteMapping("/admin/products/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @PostMapping("/admin/products/upload-image")
    public UploadResponse uploadImage(@RequestParam("file") MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            String savedFileName = productImageService.uploadImage(multipartFile.getOriginalFilename(), inputStream);
            return new UploadResponse(savedFileName);
        } catch (IOException e) {
            throw new RuntimeException("coś poszło źle podczas wgrywania pliku", e);
        }

    }

    @GetMapping("/data/productImage/{filename}")
    public ResponseEntity<Resource> serveFiles(@PathVariable String filename) throws IOException {
        Resource file = productImageService.serveFiles(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(Path.of(filename)))
                .body(file);
    }

    private static AdminProduct mapAdminProduct(AdminProductDTO adminProductDTO, Long id) {
        return AdminProduct.builder()
                .id(id)
                .name(adminProductDTO.getName())
                .description(adminProductDTO.getDescription())
                .category(adminProductDTO.getCategory())
                .price(adminProductDTO.getPrice())
                .currency(adminProductDTO.getCurrency())
                .image(adminProductDTO.getImage())
                .build();
    }

}
