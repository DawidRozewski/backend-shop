package com.example.shop.admin.order.controller;

import com.example.shop.admin.order.controller.dto.AdminInitDataDTO;
import com.example.shop.admin.order.controller.dto.AdminOrderDto;
import com.example.shop.admin.order.controller.mapper.AdminOrderMapper;
import com.example.shop.admin.order.controller.model.AdminOrder;
import com.example.shop.admin.order.controller.model.AdminOrderStatus;
import com.example.shop.admin.order.service.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @GetMapping
    public Page<AdminOrderDto> getOrder(Pageable pageable) {
        return AdminOrderMapper.mapToPageDtos(adminOrderService.getOrders(pageable));
    }

    @GetMapping("/{id}")
    public AdminOrder getOrders(@PathVariable Long id) {
        return adminOrderService.getOrder(id);
    }

    @PatchMapping("/{id}")
    public void patchOrder(@PathVariable Long id, @RequestBody Map<String, String> values) {
        adminOrderService.patchOrder(id, values);
    }

    @GetMapping("/initData")
    public AdminInitDataDTO getInitData() {
        return new AdminInitDataDTO(createOrderStatusesMap());
    }

    private Map<String, String> createOrderStatusesMap() {
        HashMap<String, String> statuses = new HashMap<>();
        for (AdminOrderStatus value : AdminOrderStatus.values()) {
            statuses.put(value.name(), value.getValue());
        }
        return statuses;
    }
}
