package com.example.shop.cart.service;

import com.example.shop.cart.model.Cart;
import com.example.shop.cart.model.CartItem;
import com.example.shop.cart.model.dto.CartProductDTO;
import com.example.shop.cart.repository.CartRepository;
import com.example.shop.common.model.Product;
import com.example.shop.common.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public Cart getCart(Long id) {
        return cartRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Cart addProductToCart(Long id, CartProductDTO cartProductDTO) {
        Cart cart = getInitializedCart(id);
        cart.addProduct(CartItem.builder()
                .quantity(cartProductDTO.quantity())
                .product(getProduct(cartProductDTO.productId()))
                .cartId(cart.getId())
                .build());
        return cart;
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow();
    }

    private Cart getInitializedCart(Long id) {
        if (id == null || id <= 0) {
            return cartRepository.save(Cart.builder()
                    .created(LocalDateTime.now())
                    .build());
        }
        return cartRepository.findById(id).orElseThrow();
    }

    public Cart updateCart(Long id, List<CartProductDTO> cartProductDTOList) {
        Cart cart = cartRepository.findById(id).orElseThrow();
        cart.getItems().forEach(cartItem -> {
            cartProductDTOList.stream()
                    .filter(cartProductDTO -> cartItem.getProduct().getId()
                            .equals(cartProductDTO.productId()))
                    .findFirst()
                    .ifPresent(cartProductDTO -> cartItem.setQuantity(cartProductDTO.quantity()));
        });
        return cart;
    }
}
