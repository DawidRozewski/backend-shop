package com.example.shop.admin.order.service;

import com.example.shop.admin.order.controller.model.AdminOrderStatus;

public class AdminOrderEmailMessage {

    public static String createProcessingEmailMessage(Long id, AdminOrderStatus newStatus) {
        return "Twoje zamówienie o id: " + id + " jest przetwarzane." +
                "\nStatus został zmieniony na : " + newStatus.getValue() +
                "\nTwoje zamówienie jest przetwarzane przez naszych pracowników" +
                "\nPo skompletowaniu, niezwłocznie przekażemy je do wysyłki :) " +
                "\n\nPozdrawiamy" +
                "\nSklep Shop";
    }

    public static String createCompletedEmailMessage(Long id, AdminOrderStatus newStatus) {
        return "Twoje zamówienie o id: " + id + " zostało zrealizowane." +
                "\nStatus został zmieniony na : " + newStatus.getValue() +
                "\n\nDziękujemy za zakupy i zapraszamy ponownie" +
                "\nSklep Shop";
    }

    public static String createRefundEmailMessage(Long id, AdminOrderStatus newStatus) {
        return "Twoje zamówienie o id: " + id + " zostało zwrócone." +
                "\nStatus został zmieniony na : " + newStatus.getValue() +
                "\n\nDziękujemy za zakupy i zapraszamy ponownie" +
                "\nSklep Shop";
    }


}
