package com.example.shop.admin.order.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class AdminInitDataDTO {
    private Map<String, String> orderStatuses;
}
