package com.example.shop.order.service;

import com.example.shop.common.mail.EmailClientService;
import com.example.shop.common.mail.FakeEmailService;
import com.example.shop.common.model.Cart;
import com.example.shop.common.model.CartItem;
import com.example.shop.common.model.Product;
import com.example.shop.common.repository.CartItemRepository;
import com.example.shop.common.repository.CartRepository;
import com.example.shop.order.model.Order;
import com.example.shop.order.model.OrderStatus;
import com.example.shop.order.model.Payment;
import com.example.shop.order.model.PaymentType;
import com.example.shop.order.model.Shipment;
import com.example.shop.order.model.dto.OrderDTO;
import com.example.shop.order.model.dto.OrderSummary;
import com.example.shop.order.repository.OrderRepository;
import com.example.shop.order.repository.OrderRowRepository;
import com.example.shop.order.repository.PaymentRepository;
import com.example.shop.order.repository.ShipmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRowRepository orderRowRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private EmailClientService emailClientService;
    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldPlaceOrder() {
        //given
        OrderDTO orderDTO = createOrderDTO();
        when(cartRepository.findById(any())).thenReturn(createCart());
        when(shipmentRepository.findById(any())).thenReturn(createShipment());
        when(paymentRepository.findById(any())).thenReturn(createPayment());
        when(orderRepository.save(any())).thenAnswer(invocationOnMock -> {
            Order argument = (Order) invocationOnMock.getArguments()[0];
            argument.setId(1L);
            return argument;
        });
        when(emailClientService.getInstance()).thenReturn(new FakeEmailService());
        //when
        OrderSummary orderSummary = orderService.placeOrder(orderDTO);
        //then
        assertThat(orderSummary).isNotNull();
        assertThat(orderSummary.getStatus()).isEqualTo(OrderStatus.NEW);
        assertThat(orderSummary.getGrossValue()).isEqualTo(new BigDecimal("36.22"));
        assertThat(orderSummary.getPayment().getType()).isEqualTo(PaymentType.BANK_TRANSFER);
        assertThat(orderSummary.getPayment().getName()).isEqualTo("test payment");
        assertThat(orderSummary.getPayment().getId()).isEqualTo(1L);
    }

    private Optional<Payment> createPayment() {
        return Optional.of(Payment.builder()
                .id(1L)
                .name("test payment")
                .type(PaymentType.BANK_TRANSFER)
                .defaultPayment(true)
                .build());
    }

    private Optional<Shipment> createShipment() {
        return Optional.of(Shipment.builder()
                .id(2L)
                .price(new BigDecimal("14.00"))
                .build());

    }

    private Optional<Cart> createCart() {
        return Optional.of(Cart.builder()
                .id(1L)
                .created(LocalDateTime.now())
                .items(createItems())
                .build());

    }

    private List<CartItem> createItems() {
        return List.of(
                CartItem.builder()
                        .id(1L)
                        .cartId(1L)
                        .quantity(1)
                        .product(Product.builder()
                                .id(1L)
                                .price(new BigDecimal("11.11"))
                                .build())
                        .build(),
                CartItem.builder()
                        .id(2L)
                        .cartId(1L)
                        .quantity(1)
                        .product(Product.builder()
                                .id(2L)
                                .price(new BigDecimal("11.11"))
                                .build())
                        .build()
        );
    }

    private static OrderDTO createOrderDTO() {
        return OrderDTO.builder()
                .firstname("firstname")
                .lastname("lastname")
                .street("street")
                .zipcode("zipcode")
                .city("city")
                .email("email")
                .phone("phone")
                .cartId(1L)
                .shipmentId(2L)
                .paymentId(3L)
                .build();
    }
}

