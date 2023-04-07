package com.example.shop.order.model.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NotificationReceiveDTO {
    private Integer merchantId;
    private Integer posId;
    private String sessionId;
    private Integer amount;
    private Integer originAmount;
    private String currency;
    private Integer orderId;
    private String methodId;
    private String statement;
    private String sign;
}
