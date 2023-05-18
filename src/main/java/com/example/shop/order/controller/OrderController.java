package com.example.shop.order.controller;

import com.example.shop.common.mail.EmailSimpleService;
import com.example.shop.common.model.OrderStatus;
import com.example.shop.order.controller.dto.NotificationDTO;
import com.example.shop.order.model.InitOrder;
import com.example.shop.order.model.Order;
import com.example.shop.order.model.dto.NotificationReceiveDTO;
import com.example.shop.order.model.dto.OrderDTO;
import com.example.shop.order.model.dto.OrderListDTO;
import com.example.shop.order.model.dto.OrderSummary;
import com.example.shop.order.service.OrderService;
import com.example.shop.order.service.PaymentService;
import com.example.shop.order.service.ShipmentService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@AllArgsConstructor
@Validated
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

    @GetMapping
    public List<OrderListDTO> getOrders(@AuthenticationPrincipal Long userId) {
        return orderService.getOrdersForCustomer(userId);
    }

    @GetMapping("/notification/{orderHash}")
    public NotificationDTO notificationShow(@PathVariable @Length(max = 12) String orderHash) {
        Order order = orderService.getOrderByOrderHash(orderHash);
        return new NotificationDTO(order.getOrderStatus() == OrderStatus.PAID);

    }

    @PostMapping("/notification/{orderHash}")
    public void notificationReceive(@PathVariable @Length(max = 12) String orderHash,
                                    @RequestBody NotificationReceiveDTO receiveDTO,
                                    HttpServletRequest request
    ) {
        String forwardedAddress = request.getHeader("x-forwarded-for");
        orderService.receiveNotification(orderHash, receiveDTO,
                StringUtils.isNoneEmpty(forwardedAddress) ? forwardedAddress : request.getRemoteAddr()
        );
    }
}
