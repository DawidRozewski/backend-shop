package com.example.shop.order.service;

import com.example.shop.common.model.Cart;
import com.example.shop.common.model.CartItem;
import com.example.shop.common.repository.CartItemRepository;
import com.example.shop.common.repository.CartRepository;
import com.example.shop.order.model.Order;
import com.example.shop.order.model.OrderRow;
import com.example.shop.order.model.OrderStatus;
import com.example.shop.order.model.dto.OrderDTO;
import com.example.shop.order.model.dto.OrderSummary;
import com.example.shop.order.repository.OrderRepository;
import com.example.shop.order.repository.OrderRowRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderRowRepository orderRowRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public OrderSummary placeOrder(OrderDTO orderDTO) {
        // stworzenie zamówienia z wierszami
        Cart cart = cartRepository.findById(orderDTO.getCartId()).orElseThrow();
        Order order = Order.builder()
                .firstname(orderDTO.getFirstname())
                .lastname(orderDTO.getLastname())
                .street(orderDTO.getStreet())
                .zipcode(orderDTO.getZipcode())
                .city(orderDTO.getCity())
                .email(orderDTO.getEmail())
                .phone(orderDTO.getPhone())
                .placeDate(LocalDateTime.now())
                .orderStatus(OrderStatus.NEW)
                .grossValue(calculateGrossValue(cart.getItems()))
                .build();
        // zapisać zamówienie
        Order newOrder = orderRepository.save(order);
        // pobrać koszyka
        saveOrderRows(cart, newOrder.getId());
        // usunąć koszyk
        cartItemRepository.deleteByCartId(orderDTO.getCartId());
        cartRepository.deleteCartById(orderDTO.getCartId());
        // zwrócic podsumowanie
        return OrderSummary.builder()
                .id(newOrder.getId())
                .placeDate(newOrder.getPlaceDate())
                .status(newOrder.getOrderStatus())
                .grossValue(newOrder.getGrossValue())
                .build();
    }

    private BigDecimal calculateGrossValue(List<CartItem> items) {
        return items.stream()
                .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private void saveOrderRows(Cart cart, Long id) {
        cart.getItems().stream()
                .map(cartItem -> OrderRow.builder()
                        .orderId(id)
                        .quantity(cartItem.getQuantity())
                        .productId(cartItem.getProduct().getId())
                        .price(cartItem.getProduct().getPrice())
                        .build()
                )
                .peek(orderRowRepository::save)
                .toList();
    }
}
