package com.wewe.wegologistic.controller;

import com.wewe.wegologistic.entity.Delivery;
import com.wewe.wegologistic.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@CrossOrigin(origins = "*") // สำหรับ frontend เรียกใช้ API
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @GetMapping
    public List<Delivery> getAll() {
        return deliveryService.getAllDeliveries();
    }

    @GetMapping("/{id}")
    public Delivery getById(@PathVariable Long id) {
        return deliveryService.getDeliveryById(id);
    }

    @PostMapping
    public Delivery create(@RequestBody Delivery delivery) {
        return deliveryService.createDelivery(delivery);
    }

    @PutMapping("/{id}")
    public Delivery update(@PathVariable Long id, @RequestBody Delivery delivery) {
        return deliveryService.updateDelivery(id, delivery);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        deliveryService.deleteDelivery(id);
    }
}

