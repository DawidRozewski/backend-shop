package com.example.shop.cart.controller.mapper;

import com.example.shop.cart.controller.dto.CartSummaryDTO;
import com.example.shop.cart.controller.dto.CartSummaryItemDTO;
import com.example.shop.cart.controller.dto.ProductDTO;
import com.example.shop.cart.controller.dto.SummaryDTO;
import com.example.shop.cart.model.Cart;
import com.example.shop.cart.model.CartItem;
import com.example.shop.common.model.Product;

import java.math.BigDecimal;
import java.util.List;

public class CartMapper {
    public static CartSummaryDTO mapToCartSummary(Cart cart) {
        return CartSummaryDTO.builder()
                .id(cart.getId())
                .items(mapCartItems(cart.getItems()))
                .summary(mapToSummary(cart.getItems()))
                .build();

    }


    private static List<CartSummaryItemDTO> mapCartItems(List<CartItem> items) {
        return items.stream()
                .map(CartMapper::mapToCartItem)
                .toList();
    }

    private static CartSummaryItemDTO mapToCartItem(CartItem cartItem) {
        return CartSummaryItemDTO.builder()
                .id(cartItem.getId())
                .quantity(cartItem.getQuantity())
                .product(mapToProductDTO(cartItem.getProduct()))
                .lineValue(calcualteLineValue(cartItem))
                .build();
    }

    private static ProductDTO mapToProductDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .currency(product.getCurrency())
                .image(product.getImage())
                .price(product.getPrice())
                .slug(product.getSlug())
                .build();
    }

    private static SummaryDTO mapToSummary(List<CartItem> items) {
        return SummaryDTO.builder()
                .grossValue(sumValues(items))
                .build();
    }

    private static BigDecimal calcualteLineValue(CartItem cartItem) {
        return cartItem.getProduct().getPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
    }

    private static BigDecimal sumValues(List<CartItem> items) {
        return items.stream()
                .map(CartMapper::calcualteLineValue)
                .reduce(BigDecimal::add)
                .orElseThrow();
    }
}
