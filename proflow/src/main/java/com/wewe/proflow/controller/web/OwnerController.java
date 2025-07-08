package com.wewe.proflow.controller.web;

import com.wewe.proflow.dto.OwnerDTO;
import com.wewe.proflow.model.Owner;
import com.wewe.proflow.service.OwnerService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/owners")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    // แสดงรายชื่อ owner ทั้งหมด
    @GetMapping
    public String listOwners(Model model) {
        List<OwnerDTO> owners = ownerService.getAllOwners().stream().map(this::toDTO).collect(Collectors.toList());
        model.addAttribute("owners", owners);
        return "owners/list";
    }

    @GetMapping("/select")
    public String selectOwner(Model model) {
        List<OwnerDTO> owners = ownerService.getAllOwners().stream().map(this::toDTO).collect(Collectors.toList());
        model.addAttribute("owners", owners);
        model.addAttribute("pageTitle", "Select Owner");
        return "owners/select";  // ชื่อไฟล์ thymeleaf
    }

    // ฟอร์มสร้าง owner ใหม่
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("owner", new OwnerDTO());
        return "owners/create";
    }

    // บันทึก owner ใหม่
    @PostMapping("/create")
    public String createOwner(@Valid @ModelAttribute("owner") OwnerDTO dto,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "owners/create";
        }

        Owner owner = new Owner();
        BeanUtils.copyProperties(dto, owner);
        ownerService.createOwner(owner);
        return "redirect:/owners";
    }

    // ฟอร์มแก้ไข owner
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Owner owner = ownerService.getOwnerById(id)
                .orElseThrow(() -> new RuntimeException("Owner not found with id: " + id));
        model.addAttribute("owner", toDTO(owner));
        return "owners/create";
    }

    // อัปเดต owner
    @PostMapping("/{id}/edit")
    public String updateOwner(@PathVariable Long id,
                              @Valid @ModelAttribute("owner") OwnerDTO dto,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "owners/create";
        }

        Owner updatedOwner = new Owner();
        BeanUtils.copyProperties(dto, updatedOwner);
        ownerService.updateOwner(id, updatedOwner);
        return "redirect:/owners";
    }

    // ลบ owner
    @PostMapping("/{id}/delete")
    public String deleteOwner(@PathVariable Long id) {
        ownerService.deleteOwner(id);
        return "redirect:/owners";
    }

    // แปลง Entity -> DTO
    private OwnerDTO toDTO(Owner owner) {
        OwnerDTO dto = new OwnerDTO();
        BeanUtils.copyProperties(owner, dto);
        return dto;
    }
}
