package com.example.shop.admin.order.service;

import com.example.shop.admin.order.controller.model.AdminOrder;
import com.example.shop.admin.order.controller.model.AdminOrderStatus;
import com.example.shop.common.mail.EmailClientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.shop.admin.order.service.AdminOrderEmailMessage.createCompletedEmailMessage;
import static com.example.shop.admin.order.service.AdminOrderEmailMessage.createProcessingEmailMessage;
import static com.example.shop.admin.order.service.AdminOrderEmailMessage.createRefundEmailMessage;

@Service
@AllArgsConstructor
class EmailNotificationForStatusChange {

    private final EmailClientService emailClientService;

    void sendEmailNotification(AdminOrderStatus newStatus, AdminOrder adminOrder) {
        if (newStatus == AdminOrderStatus.PROCESSING) {
            sendEmail(adminOrder.getEmail(),
                    "Zamówienie " + adminOrder.getId() + " zmieniło status na: " + newStatus.getValue(),
                    createProcessingEmailMessage(adminOrder.getId(), newStatus));
        } else if (newStatus == AdminOrderStatus.COMPLETED) {
            sendEmail(adminOrder.getEmail(),
                    "Zamówienie " + adminOrder.getId() + " zostało zrealizowane " + newStatus.getValue(),
                    createCompletedEmailMessage(adminOrder.getId(), newStatus));
        } else if (newStatus == AdminOrderStatus.REFUND) {
            sendEmail(adminOrder.getEmail(),
                    "Zamówienie " + adminOrder.getId() + " zmieniło status na: " + newStatus.getValue(),
                    createRefundEmailMessage(adminOrder.getId(), newStatus));
        }

    }

    private void sendEmail(String email, String subject, String message) {
        emailClientService.getInstance().send(email, subject, message);
    }
}
