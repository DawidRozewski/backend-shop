package com.example.shop.order.service.mapper;

import com.example.shop.order.model.Order;
import com.example.shop.order.model.dto.OrderListDTO;

import java.util.List;

public class OrderDTOMapper {

    public static List<OrderListDTO> mapToOrderListDTO(List<Order> orders) {
        return orders.stream()
                .map(order -> new OrderListDTO(
                        order.getId(),
                        order.getPlaceDate(),
                        order.getOrderStatus().getValue(),
                        order.getGrossValue()))
                .toList();
    }
}
