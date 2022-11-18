package com.example.shop.admin.product.controller.dto;


import com.example.shop.admin.product.model.AdminProductCurrency;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
public class AdminProductDTO {

    @NotBlank
    @Length(min = 4)
    private String name;

    @NotNull
    private Long categoryId;

    @NotBlank
    @Length(min = 4)
    private String description;

    private String fullDescription;

    @NotNull
    @Min(4)
    private BigDecimal price;

    private AdminProductCurrency currency;

    private String image;

    @NotBlank
    @Length(min = 4)
    private String slug;
}
