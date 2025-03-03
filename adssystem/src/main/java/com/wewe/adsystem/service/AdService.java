package com.wewe.adsystem.service;

import com.wewe.adsystem.model.Ad;
import com.wewe.adsystem.model.Status;
import com.wewe.adsystem.repository.AdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdService {

    @Autowired
    private AdRepository adRepository;

    // ดึงโฆษณาทั้งหมด
    public List<Ad> getAllAds() {
        return adRepository.findAll();
    }

    // ดึงโฆษณาที่ Active เท่านั้น
    public List<Ad> getActiveAds() {
        return adRepository.findByStatus(Status.ACTIVE);
    }

    // ดึงโฆษณาตาม ID
    public Optional<Ad> getAdById(Long id) {
        return adRepository.findById(id);
    }

    // สร้างโฆษณาใหม่
    public Ad createAd(Ad ad) {
        return adRepository.save(ad);
    }

    // อัปเดตโฆษณา
    public Ad updateAd(Long id, Ad adDetails) {
        Optional<Ad> adOptional = adRepository.findById(id);
        if (adOptional.isPresent()) {
            Ad ad = adOptional.get();
            ad.setTitle(adDetails.getTitle());
            ad.setDescription(adDetails.getDescription());
            ad.setImageUrl(adDetails.getImageUrl());
            ad.setTargetUrl(adDetails.getTargetUrl());
            ad.setStartDate(adDetails.getStartDate());
            ad.setEndDate(adDetails.getEndDate());
            ad.setStatus(adDetails.getStatus());
            return adRepository.save(ad);
        }
        return null;
    }

    // ลบโฆษณา
    public boolean deleteAd(Long id) {
        if (adRepository.existsById(id)) {
            adRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
