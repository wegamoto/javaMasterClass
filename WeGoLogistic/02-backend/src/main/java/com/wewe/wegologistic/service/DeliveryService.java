package com.wewe.wegologistic.service;

import com.wewe.wegologistic.entity.Delivery;

import java.util.List;

public interface DeliveryService {
    List<Delivery> getAllDeliveries();

    Delivery getDeliveryById(Long id);

    Delivery createDelivery(Delivery delivery);

    Delivery updateDelivery(Long id, Delivery delivery);

    void deleteDelivery(Long id);
}
