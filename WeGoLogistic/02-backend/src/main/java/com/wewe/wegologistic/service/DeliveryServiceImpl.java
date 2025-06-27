package com.wewe.wegologistic.service;

import com.wewe.wegologistic.entity.Delivery;
import com.wewe.wegologistic.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Override
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    @Override
    public Delivery getDeliveryById(Long id) {
        return deliveryRepository.findById(id).orElse(null);
    }

    @Override
    public Delivery createDelivery(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    @Override
    public Delivery updateDelivery(Long id, Delivery delivery) {
        Delivery existing = deliveryRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setSenderName(delivery.getSenderName());
            existing.setReceiverName(delivery.getReceiverName());
            existing.setOrigin(delivery.getOrigin());
            existing.setDestination(delivery.getDestination());
            existing.setDeliveryDate(delivery.getDeliveryDate());
            existing.setStatus(delivery.getStatus());
            return deliveryRepository.save(existing);
        }
        return null;
    }

    @Override
    public void deleteDelivery(Long id) {
        deliveryRepository.deleteById(id);
    }
}

