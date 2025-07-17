package com.wewe.promptinvoice.service;

import com.wewe.promptinvoice.model.Setting;
import com.wewe.promptinvoice.repository.SettingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SettingService {

    private final SettingRepository settingRepository;

    public SettingService(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public List<Setting> findAll() {
        return settingRepository.findAll();
    }

    public Optional<Setting> findById(Long id) {
        return settingRepository.findById(id);
    }

    public Setting save(Setting setting) {
        return settingRepository.save(setting);
    }

    public void deleteById(Long id) {
        settingRepository.deleteById(id);
    }

    public Optional<Setting> findByKey(String key) {
        return settingRepository.findByKey(key);
    }

    public String getValue(String key, String defaultValue) {
        return settingRepository.findByKey(key)
                .map(Setting::getValue)
                .orElse(defaultValue);
    }

    public String getPromptPayPhone() {
        return getValue("promptpay.phone", "0000000000"); // default fallback
    }
}
