package com.example.shop.order.service.payment.p24;

import com.example.shop.order.model.Order;
import com.example.shop.order.model.dto.NotificationReceiveDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.example.shop.order.service.payment.p24.RequestUtil.createRegisterRequest;
import static com.example.shop.order.service.payment.p24.RequestUtil.createVerifyRequest;
import static com.example.shop.order.service.payment.p24.RequestUtil.validate;
import static com.example.shop.order.service.payment.p24.RequestUtil.validateIpAddress;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentMethodP24 {

    private final PaymentMethodP24Config config;
    private final WebClient p24Client;

    public String initPayment(Order newOrder) {
        log.info("Payment initialization");

        ResponseEntity<TransactionRegisterResponse> result = p24Client.post().uri("/transaction/register")
                .bodyValue(createRegisterRequest(config, newOrder))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        clientResponse -> {
                            log.error("Something went wrong: " + clientResponse.statusCode().name());
                            return Mono.empty();
                        })
                .toEntity(TransactionRegisterResponse.class)
                .block();
        if (result != null &&
                result.getBody() != null &&
                result.getBody().getData() != null) {
            return (config.isTestMode() ? config.getTestUrl() : config.getUrl()) + "/trnRequest/" +
                    result.getBody().getData().token();
        }
        return null;
    }

    public String receiveNotification(Order order, NotificationReceiveDTO receiveDTO, String remoteAddr) {
        log.info(receiveDTO.toString());
        validateIpAddress(remoteAddr, config);
        validate(receiveDTO, order, config);
        return verifyPayment(receiveDTO, order);
    }

    private String verifyPayment(NotificationReceiveDTO receiveDTO, Order order) {
        ResponseEntity<TransactionVerifyResponse> result = p24Client.put().uri("/transaction/verify")
                .bodyValue(createVerifyRequest(config, order, receiveDTO))
                .retrieve()
                .toEntity(TransactionVerifyResponse.class)
                .block();
        log.info("Verification status transaction " + result.getBody().getData().status());
        return result.getBody().getData().status();
    }

}
