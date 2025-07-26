package com.wewe.swiftaid.controller;

import com.wewe.swiftaid.entity.MedicalSupply;
import com.wewe.swiftaid.repository.AmbulanceTeamRepository;
import com.wewe.swiftaid.repository.MedicalSupplyRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/medicalsupply")  // เปลี่ยน URL จาก /supplies เป็น /medicalsupply
public class MedicalSupplyController {

    private final MedicalSupplyRepository medicalSupplyRepository;
    private final AmbulanceTeamRepository ambulanceTeamRepository;

    public MedicalSupplyController(MedicalSupplyRepository supplyRepo, AmbulanceTeamRepository teamRepo) {
        this.medicalSupplyRepository = supplyRepo;
        this.ambulanceTeamRepository = teamRepo;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("medicalSupply", medicalSupplyRepository.findAll());  // เปลี่ยนชื่อ attribute ให้สื่อความหมายดีขึ้น
        return "medicalsupply/list";  // แก้ path ให้ตรงกับโฟลเดอร์และไฟล์ของเทมเพลต
    }

    // 🔹 แสดงฟอร์มเพิ่ม
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("medicalSupply", new MedicalSupply()); // ชื่อให้ตรงกับ Thymeleaf
        model.addAttribute("teams", ambulanceTeamRepository.findAll());
        return "medicalsupply/form"; // ชี้ไปที่ templates/medicalsupply/form.html
    }

    // 🔹 บันทึกเวชภัณฑ์
    @PostMapping
    public String save(@ModelAttribute MedicalSupply medicalSupply) {
        medicalSupplyRepository.save(medicalSupply);
        return "redirect:/medicalsupply";  // redirect ให้ตรงกับ URL ใหม่
    }

    // 🔹 แสดงฟอร์มแก้ไข
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        MedicalSupply supply = medicalSupplyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid supply Id:" + id));
        model.addAttribute("medicalSupply", supply);
        model.addAttribute("teams", ambulanceTeamRepository.findAll());
        return "medicalsupply/form";
    }

    // 🔹 ลบเวชภัณฑ์
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        medicalSupplyRepository.deleteById(id);
        return "redirect:/medicalsupply";
    }
}
