package com.example.shop.admin.order.service;

import com.example.shop.admin.order.model.AdminOrder;
import com.example.shop.common.mail.EmailClientService;
import com.example.shop.common.model.OrderStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.shop.admin.order.service.AdminOrderEmailMessage.createCompletedEmailMessage;
import static com.example.shop.admin.order.service.AdminOrderEmailMessage.createProcessingEmailMessage;
import static com.example.shop.admin.order.service.AdminOrderEmailMessage.createRefundEmailMessage;

@Service
@AllArgsConstructor
class EmailNotificationForStatusChange {

    private final EmailClientService emailClientService;

    void sendEmailNotification(OrderStatus newStatus, AdminOrder adminOrder) {
        if (newStatus == OrderStatus.PROCESSING) {
            sendEmail(adminOrder.getEmail(),
                    "Zamówienie " + adminOrder.getId() + " zmieniło status na: " + newStatus.getValue(),
                    createProcessingEmailMessage(adminOrder.getId(), newStatus));
        } else if (newStatus == OrderStatus.COMPLETED) {
            sendEmail(adminOrder.getEmail(),
                    "Zamówienie " + adminOrder.getId() + " zostało zrealizowane " + newStatus.getValue(),
                    createCompletedEmailMessage(adminOrder.getId(), newStatus));
        } else if (newStatus == OrderStatus.REFUND) {
            sendEmail(adminOrder.getEmail(),
                    "Zamówienie " + adminOrder.getId() + " zmieniło status na: " + newStatus.getValue(),
                    createRefundEmailMessage(adminOrder.getId(), newStatus));
        }

    }

    private void sendEmail(String email, String subject, String message) {
        emailClientService.getInstance().send(email, subject, message);
    }
}
