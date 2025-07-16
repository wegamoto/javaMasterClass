package com.wewe.marketflow.service.impl;

import com.wewe.marketflow.model.AppSetting;
import com.wewe.marketflow.repository.AppSettingRepository;
import com.wewe.marketflow.service.AppSettingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppSettingServiceImpl implements AppSettingService {

    private final AppSettingRepository settingRepository;

    public AppSettingServiceImpl(AppSettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    @Override
    public List<AppSetting> getAllSettings() {
        return settingRepository.findAll();
    }

    @Override
    public AppSetting getSettingByKey(String key) {
        return settingRepository.findByKey(key).orElse(null);
    }

    @Override
    public AppSetting save(AppSetting setting) {
        return settingRepository.save(setting);
    }

    @Override
    public void delete(Long id) {
        settingRepository.deleteById(id);
    }
}
