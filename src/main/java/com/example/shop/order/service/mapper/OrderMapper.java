package com.example.shop.order.service.mapper;

import com.example.shop.common.model.Cart;
import com.example.shop.common.model.CartItem;
import com.example.shop.common.model.OrderStatus;
import com.example.shop.order.model.Order;
import com.example.shop.order.model.OrderRow;
import com.example.shop.order.model.Payment;
import com.example.shop.order.model.Shipment;
import com.example.shop.order.model.dto.OrderDTO;
import com.example.shop.order.model.dto.OrderSummary;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderMapper {
    public static Order createNewOrder(OrderDTO orderDTO, Cart cart, Shipment shipment, Payment payment, Long userId) {
        return Order.builder()
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
                .userId(userId)
                .orderHash(RandomStringUtils.randomAlphabetic(12))
                .build();
    }

    public static BigDecimal calculateGrossValue(List<CartItem> items, Shipment shipment) {
        return items.stream()
                .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO)
                .add(shipment.getPrice());
    }

    public static OrderSummary createOrderSummary(Payment payment, Order newOrder, String redirectUrl) {
        return OrderSummary.builder()
                .id(newOrder.getId())
                .placeDate(newOrder.getPlaceDate())
                .status(newOrder.getOrderStatus())
                .grossValue(newOrder.getGrossValue())
                .payment(payment)
                .redirectUrl(redirectUrl)
                .build();
    }

    public static OrderRow mapToOrderRow(Long orderId, Shipment shipment) {
        return OrderRow.builder()
                .quantity(1)
                .price(shipment.getPrice())
                .shipmentId(shipment.getId())
                .orderId(orderId)
                .build();
    }

    public static OrderRow mapToOrderRowWithQuantity(Long orderId, CartItem cartItem) {
        return OrderRow.builder()
                .orderId(orderId)
                .quantity(cartItem.getQuantity())
                .productId(cartItem.getProduct().getId())
                .price(cartItem.getProduct().getPrice())
                .build();
    }
}


