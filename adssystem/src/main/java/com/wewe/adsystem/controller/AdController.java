package com.wewe.adsystem.controller;

import com.wewe.adsystem.model.Ad;
import com.wewe.adsystem.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ads")
public class AdController {

    @Autowired
    private AdService adService;

    // ดึงโฆษณาทั้งหมด
    @GetMapping
    public List<Ad> getAllAds() {
        return adService.getAllAds();
    }

    // ดึงโฆษณาที่ Active เท่านั้น
    @GetMapping("/active")
    public List<Ad> getActiveAds() {
        return adService.getActiveAds();
    }

    // ดึงโฆษณาตาม ID
    @GetMapping("/{id}")
    public ResponseEntity<Ad> getAdById(@PathVariable Long id) {
        Optional<Ad> ad = adService.getAdById(id);
        return ad.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // สร้างโฆษณาใหม่
    @PostMapping
    public Ad createAd(@RequestBody Ad ad) {
        return adService.createAd(ad);
    }

    // อัปเดตโฆษณา
    @PutMapping("/{id}")
    public ResponseEntity<Ad> updateAd(@PathVariable Long id, @RequestBody Ad adDetails) {
        Ad updatedAd = adService.updateAd(id, adDetails);
        return updatedAd != null ? ResponseEntity.ok(updatedAd) : ResponseEntity.notFound().build();
    }

    // ลบโฆษณา
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable Long id) {
        return adService.deleteAd(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}


