package com.wewe.promptinvoice.controller;

import com.wewe.promptinvoice.model.Setting;
import com.wewe.promptinvoice.service.SettingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/settings")
public class SettingController {

    private final SettingService settingService;

    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @GetMapping
    public String listSettings(Model model, HttpServletRequest request) {
        model.addAttribute("settings", settingService.findAll());
        model.addAttribute("requestURI", request.getRequestURI());
        return "settings/list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model, HttpServletRequest request) {
        model.addAttribute("setting", new Setting());
        model.addAttribute("requestURI", request.getRequestURI());
        return "settings/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpServletRequest request) {
        Setting setting = settingService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid setting Id: " + id));
        model.addAttribute("setting", setting);
        model.addAttribute("requestURI", request.getRequestURI());
        return "settings/form";
    }

    @PostMapping("/save")
    public String saveSetting(@Valid @ModelAttribute("setting") Setting setting,
                              BindingResult bindingResult,
                              Model model,
                              HttpServletRequest request) {
        model.addAttribute("requestURI", request.getRequestURI());

        if (bindingResult.hasErrors()) {
            return "settings/form";
        }

        settingService.save(setting);
        return "redirect:/settings";
    }

    @GetMapping("/delete/{id}")
    public String deleteSetting(@PathVariable Long id) {
        settingService.deleteById(id);
        return "redirect:/settings";
    }
}
