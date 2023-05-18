package com.example.shop.order.service.payment.p24;

import com.example.shop.order.model.Order;
import com.example.shop.order.model.dto.NotificationReceiveDTO;
import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigDecimal;

public class RequestUtil {


    public static TransactionRegisterRequest createRegisterRequest(PaymentMethodP24Config config, Order newOrder) {
        return TransactionRegisterRequest.builder()
                .merchantId(config.getMerchantId())
                .posId(config.getPosId())
                .sessionId(createSessionId(newOrder))
                .amount(newOrder.getGrossValue().movePointRight(2).intValue())
                .currency("PLN")
                .description("Zamówienie id:" + newOrder.getId())
                .email(newOrder.getEmail())
                .client(newOrder.getFirstname() + " " + newOrder.getLastname())
                .country("PL")
                .language("pl")
                .urlReturn(generateReturnUrl(newOrder.getOrderHash(), config))
                .urlStatus(generateStatusUrl(newOrder.getOrderHash(), config))
                .sign(createSign(newOrder, config))
                .encoding("UTF-8")
                .build();
    }

    public static TransactionVerifyRequest createVerifyRequest(PaymentMethodP24Config config, Order order, NotificationReceiveDTO receiveDTO) {
        return TransactionVerifyRequest.builder()
                .merchantId(config.getMerchantId())
                .posId(config.getPosId())
                .sessionId(createSessionId(order))
                .amount(order.getGrossValue().movePointRight(2).intValue())
                .currency("PLN")
                .orderId(receiveDTO.getOrderId())
                .sign(createVerifySign(receiveDTO, order, config))
                .build();
    }

    private static String generateStatusUrl(String orderHash, PaymentMethodP24Config config) {
        String baseUrl = config.isTestMode() ? config.getTestUrlStatus() : config.getUrlStatus();
        return baseUrl + "/orders/notification/" + orderHash;
    }

    private static String generateReturnUrl(String orderHash, PaymentMethodP24Config config) {
        String baseUrl = config.isTestMode() ? config.getTestUrlReturn() : config.getUrlReturn();
        return baseUrl + "/order/notification/" + orderHash;
    }


    private static String createSign(Order newOrder, PaymentMethodP24Config config) {
        String json = "{\"sessionId\":\"" + createSessionId(newOrder) +
                "\",\"merchantId\":" + config.getMerchantId() +
                ",\"amount\":" + newOrder.getGrossValue().movePointRight(2).intValue() +
                ",\"currency\":\"PLN\",\"crc\":\"" + (config.isTestMode() ? config.getTestCrc() : config.getCrc()) + "\"}";
        return DigestUtils.sha384Hex(json);
    }

    private static String createSessionId(Order newOrder) {
        return "order_id_" + newOrder.getId().toString();
    }


    private static String createVerifySign(NotificationReceiveDTO receiveDTO, Order order, PaymentMethodP24Config config) {
        String json = "{\"sessionId\":\"" + createSessionId(order) +
                "\",\"orderId\":" + receiveDTO.getOrderId() +
                ",\"amount\":" + order.getGrossValue().movePointRight(2).intValue() +
                ",\"currency\":\"PLN\",\"crc\":\"" + (config.isTestMode() ? config.getTestCrc() : config.getCrc()) + "\"}";
        return DigestUtils.sha384Hex(json);
    }


    public static void validate(NotificationReceiveDTO receiveDTO, Order order, PaymentMethodP24Config config) {
        validateField(config.getMerchantId().equals(receiveDTO.getMerchantId()));
        validateField(config.getPosId().equals(receiveDTO.getPosId()));
        validateField(createSessionId(order).equals(receiveDTO.getSessionId()));
        validateField(order.getGrossValue().compareTo(BigDecimal.valueOf(receiveDTO.getAmount()).movePointLeft(2)) == 0);
        validateField(order.getGrossValue().compareTo(BigDecimal.valueOf(receiveDTO.getOriginAmount()).movePointLeft(2)) == 0);
        validateField("PLN".equals(receiveDTO.getCurrency()));
        validateField(createReceivedSign(receiveDTO, order, config).equals(receiveDTO.getSign()));
    }

    private static String createReceivedSign(NotificationReceiveDTO receiveDTO, Order order, PaymentMethodP24Config config) {
        String json = "{\"merchantId\":" + config.getMerchantId() +
                ",\"posId\":" + config.getPosId() +
                ",\"sessionId\":\"" + createSessionId(order) +
                "\",\"amount\":" + order.getGrossValue().movePointRight(2).intValue() +
                ",\"originAmount\":" + order.getGrossValue().movePointRight(2).intValue() +
                ",\"currency\":\"PLN\"" +
                ",\"orderId\":" + receiveDTO.getOrderId() +
                ",\"methodId\":" + receiveDTO.getMethodId() +
                ",\"statement\":\"" + receiveDTO.getStatement() +
                "\",\"crc\":\"" + (config.isTestMode() ? config.getTestCrc() : config.getCrc()) + "\"}";

        return DigestUtils.sha384Hex(json);
    }

    private static void validateField(boolean condition) {
        if (!condition) {
            throw new RuntimeException("Validation failed");
        }
    }


    static void validateIpAddress(String remoteAddr, PaymentMethodP24Config config) {
        if (config.getServers().contains(remoteAddr)) {
            throw new RuntimeException("Invalid IP address ");
        }
    }

}
