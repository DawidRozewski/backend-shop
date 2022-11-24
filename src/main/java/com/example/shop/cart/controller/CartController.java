package com.example.shop.cart.controller;

import com.example.shop.cart.controller.dto.CartSummaryDTO;
import com.example.shop.cart.controller.mapper.CartMapper;
import com.example.shop.cart.model.dto.CartProductDTO;
import com.example.shop.cart.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    @GetMapping("/{id}")
    public CartSummaryDTO getCart(@PathVariable Long id) {
        return CartMapper.mapToCartSummary(cartService.getCart(id));
    }

    @PutMapping("/{id}")
    public CartSummaryDTO addProductToCart(@PathVariable Long id, @RequestBody CartProductDTO cartProductDTO) {
        return CartMapper.mapToCartSummary(cartService.addProductToCart(id, cartProductDTO));
    }
}
