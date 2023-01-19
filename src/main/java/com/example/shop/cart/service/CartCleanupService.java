package com.example.shop.cart.service;

import com.example.shop.common.model.Cart;
import com.example.shop.common.repository.CartItemRepository;
import com.example.shop.common.repository.CartRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CartCleanupService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    @Scheduled(cron = "${app.cart.cleanup.expression}")
    public void cleanupOldCarts() {
        List<Cart> carts = cartRepository.findByCreatedLessThan(LocalDateTime.now().minusDays(3));
        List<Long> ids = carts.stream().map(Cart::getId).toList();

        log.info("Stare koszyki " + carts.size());
//        carts.forEach(cart -> {
//            cartItemRepository.deleteByCartId(cart.getId());
//            cartRepository.deleteCartById(cart.getId());
//        });
        if (!ids.isEmpty()) {
            cartItemRepository.deleteAllByCartIdIn(ids);
            cartRepository.deleteAllByIdIn(ids);
        }
    }
}
