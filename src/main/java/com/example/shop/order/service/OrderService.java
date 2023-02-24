package com.example.shop.order.service;

import com.example.shop.common.model.Cart;
import com.example.shop.common.model.CartItem;
import com.example.shop.common.repository.CartItemRepository;
import com.example.shop.common.repository.CartRepository;
import com.example.shop.order.model.Order;
import com.example.shop.order.model.OrderRow;
import com.example.shop.order.model.OrderStatus;
import com.example.shop.order.model.Payment;
import com.example.shop.order.model.Shipment;
import com.example.shop.order.model.dto.OrderDTO;
import com.example.shop.order.model.dto.OrderSummary;
import com.example.shop.order.repository.OrderRepository;
import com.example.shop.order.repository.OrderRowRepository;
import com.example.shop.order.repository.PaymentRepository;
import com.example.shop.order.repository.ShipmentRepository;
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
    private final ShipmentRepository shipmentRepository;

    private final PaymentRepository paymentRepository;

    @Transactional
    public OrderSummary placeOrder(OrderDTO orderDTO) {
        // stworzenie zamówienia z wierszami
        Cart cart = cartRepository.findById(orderDTO.getCartId()).orElseThrow();
        Shipment shipment = shipmentRepository.findById(orderDTO.getShipmentId()).orElseThrow();
        Payment payment = paymentRepository.findById(orderDTO.getPaymentId()).orElseThrow();
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
                .grossValue(calculateGrossValue(cart.getItems(), shipment))
                .payment(payment)
                .build();
        // zapisać zamówienie
        Order newOrder = orderRepository.save(order);
        saveOrderRows(cart, newOrder.getId(), shipment);


        // usunąć koszyk
        cartItemRepository.deleteByCartId(orderDTO.getCartId());
        cartRepository.deleteCartById(orderDTO.getCartId());
        // zwrócic podsumowanie
        return OrderSummary.builder()
                .id(newOrder.getId())
                .placeDate(newOrder.getPlaceDate())
                .status(newOrder.getOrderStatus())
                .grossValue(newOrder.getGrossValue())
                .payment(payment)
                .build();
    }

    private BigDecimal calculateGrossValue(List<CartItem> items, Shipment shipment) {
        return items.stream()
                .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO)
                .add(shipment.getPrice());
    }

    private void saveOrderRows(Cart cart, Long orderId, Shipment shipment) {
        saveProductRows(cart, orderId);
        saveShipmentRow(orderId, shipment);
    }

    private void saveShipmentRow(Long orderId, Shipment shipment) {
        orderRowRepository.save(OrderRow.builder()
                .quantity(1)
                .price(shipment.getPrice())
                .shipmentId(shipment.getId())
                .orderId(orderId)
                .build());
    }

    private void saveProductRows(Cart cart, Long orderId) {
        cart.getItems().stream()
                .map(cartItem -> OrderRow.builder()
                        .orderId(orderId)
                        .quantity(cartItem.getQuantity())
                        .productId(cartItem.getProduct().getId())
                        .price(cartItem.getProduct().getPrice())
                        .build()
                )
                .peek(orderRowRepository::save)
                .toList();
    }
}
