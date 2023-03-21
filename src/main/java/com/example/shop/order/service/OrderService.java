package com.example.shop.order.service;

import com.example.shop.common.mail.EmailClientService;
import com.example.shop.common.model.Cart;
import com.example.shop.common.repository.CartItemRepository;
import com.example.shop.common.repository.CartRepository;
import com.example.shop.order.model.Order;
import com.example.shop.order.model.Payment;
import com.example.shop.order.model.Shipment;
import com.example.shop.order.model.dto.OrderDTO;
import com.example.shop.order.model.dto.OrderSummary;
import com.example.shop.order.repository.OrderRepository;
import com.example.shop.order.repository.OrderRowRepository;
import com.example.shop.order.repository.PaymentRepository;
import com.example.shop.order.repository.ShipmentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.shop.order.service.mapper.OrderEmailMessage.createEmailMessage;
import static com.example.shop.order.service.mapper.OrderMapper.createNewOrder;
import static com.example.shop.order.service.mapper.OrderMapper.createOrderSummary;
import static com.example.shop.order.service.mapper.OrderMapper.mapToOrderRow;
import static com.example.shop.order.service.mapper.OrderMapper.mapToOrderRowWithQuantity;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderRowRepository orderRowRepository;
    private final CartItemRepository cartItemRepository;
    private final ShipmentRepository shipmentRepository;
    private final PaymentRepository paymentRepository;
    private final EmailClientService emailClientService;

    @Transactional
    public OrderSummary placeOrder(OrderDTO orderDTO) {
        Cart cart = cartRepository.findById(orderDTO.getCartId()).orElseThrow();
        Shipment shipment = shipmentRepository.findById(orderDTO.getShipmentId()).orElseThrow();
        Payment payment = paymentRepository.findById(orderDTO.getPaymentId()).orElseThrow();
        Order newOrder = orderRepository.save(createNewOrder(orderDTO, cart, shipment, payment));
        saveOrderRows(cart, newOrder.getId(), shipment);
        clearOrderCart(orderDTO);
        sendConfirmEmail(newOrder);
        return createOrderSummary(payment, newOrder);
    }

    private void sendConfirmEmail(Order newOrder) {
        emailClientService.getInstance()
                .send(newOrder.getEmail(),
                        "Twoje zamówienie zostało przyjęte",
                        createEmailMessage(newOrder));
    }

    private void clearOrderCart(OrderDTO orderDTO) {
        cartItemRepository.deleteByCartId(orderDTO.getCartId());
        cartRepository.deleteCartById(orderDTO.getCartId());
    }

    private void saveOrderRows(Cart cart, Long orderId, Shipment shipment) {
        saveProductRows(cart, orderId);
        saveShipmentRow(orderId, shipment);
    }

    private void saveShipmentRow(Long orderId, Shipment shipment) {
        orderRowRepository.save(mapToOrderRow(orderId, shipment));
    }

    private void saveProductRows(Cart cart, Long orderId) {
        cart.getItems().stream()
                .map(cartItem -> mapToOrderRowWithQuantity(orderId, cartItem)
                )
                .peek(orderRowRepository::save)
                .toList();
    }
}