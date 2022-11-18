package com.example.shop.admin.category.controller;

import com.example.shop.admin.category.controller.dto.AdminCategoryDTO;
import com.example.shop.admin.category.model.AdminCategory;
import com.example.shop.admin.category.service.AdminCategoryService;
import com.github.slugify.Slugify;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private static final Long EMPTY_ID = null;
    private final AdminCategoryService adminCategoryService;

    @GetMapping
    public List<AdminCategory> getCategories() {
        return adminCategoryService.getCategories();
    }

    @GetMapping("/{id}")
    public AdminCategory getCategory(@PathVariable Long id) {
        return adminCategoryService.getCategory(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        adminCategoryService.deleteCategory(id);
    }

    @PutMapping("/{id}")
    public AdminCategory updateCategory(@PathVariable Long id, AdminCategoryDTO adminCategoryDTO) {
        return adminCategoryService.updateCategory(mapToAdminCategory(id, adminCategoryDTO));
    }

    @PostMapping
    public AdminCategory createCategory(@RequestBody AdminCategoryDTO adminCategoryDTO) {
        return adminCategoryService.createCategory(mapToAdminCategory(EMPTY_ID, adminCategoryDTO));
    }

    private AdminCategory mapToAdminCategory(Long id, AdminCategoryDTO adminCategoryDTO) {
        return AdminCategory.builder()
                .id(id)
                .name(adminCategoryDTO.getName())
                .description(adminCategoryDTO.getDescription())
                .slug(slugifyCategoryName(adminCategoryDTO.getSlug()))
                .build();
    }

    private String slugifyCategoryName(String slug) {
        Slugify slugify = new Slugify();
        return slugify.withCustomReplacement("_", "-").slugify(slug);

    }
}
