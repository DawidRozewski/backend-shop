package com.example.shop.order.controller;

import com.example.shop.common.mail.EmailSimpleService;
import com.example.shop.order.model.InitOrder;
import com.example.shop.order.model.dto.OrderDTO;
import com.example.shop.order.model.dto.OrderSummary;
import com.example.shop.order.service.OrderService;
import com.example.shop.order.service.PaymentService;
import com.example.shop.order.service.ShipmentService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final ShipmentService shipmentService;
    private final PaymentService paymentService;

    private final EmailSimpleService emailSimpleService;

    @PostMapping
    public OrderSummary placeOrder(
            @RequestBody OrderDTO orderDTO,
            @AuthenticationPrincipal Long userId) {
        return orderService.placeOrder(orderDTO, userId);
    }

    @GetMapping("/initData")
    public InitOrder initData() {
        return InitOrder.builder()
                .shipment(shipmentService.getShipments())
                .payment(paymentService.getPayments())
                .build();
    }


}
