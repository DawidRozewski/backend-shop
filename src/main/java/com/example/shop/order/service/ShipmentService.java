package com.example.shop.order.service;

import com.example.shop.order.model.Shipment;
import com.example.shop.order.repository.ShipmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ShipmentService {
    private final ShipmentRepository shipmentRepository;

    public List<Shipment> getShipments() {
        return shipmentRepository.findAll();
    }
}
