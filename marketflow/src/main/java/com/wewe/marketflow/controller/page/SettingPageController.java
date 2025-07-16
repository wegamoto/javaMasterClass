package com.wewe.marketflow.controller.page;

import com.wewe.marketflow.model.AppSetting;
import com.wewe.marketflow.service.AppSettingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/settings")
public class SettingPageController {

    private final AppSettingService settingService;

    public SettingPageController(AppSettingService settingService) {
        this.settingService = settingService;
    }

    @GetMapping
    public String listSettings(Model model) {
        model.addAttribute("settings", settingService.getAllSettings());
        return "settings/list";
    }

    @GetMapping("/create")
    public String createSettingForm(Model model) {
        model.addAttribute("setting", new AppSetting());
        return "settings/form";
    }

    @GetMapping("/edit/{id}")
    public String editSettingForm(@PathVariable Long id, Model model) {
        AppSetting setting = settingService.getAllSettings()
                .stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
        model.addAttribute("setting", setting);
        return "settings/form";
    }

    @PostMapping("/save")
    public String saveSetting(@ModelAttribute AppSetting setting) {
        settingService.save(setting);
        return "redirect:/settings";
    }

    @GetMapping("/delete/{id}")
    public String deleteSetting(@PathVariable Long id) {
        settingService.delete(id);
        return "redirect:/settings";
    }
}
