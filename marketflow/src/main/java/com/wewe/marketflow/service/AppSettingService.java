package com.wewe.marketflow.service;

import com.wewe.marketflow.model.AppSetting;

import java.util.List;

public interface AppSettingService {
    List<AppSetting> getAllSettings();
    AppSetting getSettingByKey(String key);
    AppSetting save(AppSetting setting);
    void delete(Long id);
}
