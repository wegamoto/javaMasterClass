package com.servix.maintenance.controller.page;

import com.servix.maintenance.model.SparePart;
import com.servix.maintenance.repository.SparePartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/spare-parts")
@RequiredArgsConstructor
public class SparePartWebController {

    private final SparePartRepository sparePartRepository;

    @GetMapping
    public String sparePartMainPage() {
        return "spareparts/index"; // templates/spareparts/index.html
    }

    // ✅ GET: แสดงรายการอะไหล่ พร้อมค้นหาได้
    @GetMapping("/list")
    public String list(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        List<SparePart> spareParts;
        if (keyword != null && !keyword.isBlank()) {
            spareParts = sparePartRepository.findByNameContainingIgnoreCaseOrPartNumberContainingIgnoreCase(keyword, keyword);
        } else {
            spareParts = sparePartRepository.findAll();
        }
        model.addAttribute("spareParts", spareParts);
        model.addAttribute("keyword", keyword);
        return "spareparts/list";
    }

    // ✅ GET: แสดงฟอร์มสร้างใหม่
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("sparePart", new SparePart());
        return "spareparts/form";
    }

    // ✅ POST: บันทึกอะไหล่ใหม่
    @PostMapping
    public String create(@ModelAttribute SparePart sparePart) {
        sparePartRepository.save(sparePart);
        return "redirect:/spare-parts";
    }

    // ✅ GET: แสดงฟอร์มแก้ไข
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        SparePart part = sparePartRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid spare part ID: " + id));
        model.addAttribute("sparePart", part);
        return "spareparts/form";
    }

    // ✅ POST: อัปเดตข้อมูลอะไหล่
    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute SparePart updatedPart) {
        SparePart part = sparePartRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid spare part ID: " + id));
        part.setName(updatedPart.getName());
        part.setPartNumber(updatedPart.getPartNumber());
        part.setQuantityInStock(updatedPart.getQuantityInStock());
        part.setReorderLevel(updatedPart.getReorderLevel());
        sparePartRepository.save(part);
        return "redirect:/spare-parts";
    }

    // ✅ GET: ลบอะไหล่
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        sparePartRepository.deleteById(id);
        return "redirect:/spare-parts";
    }
}
