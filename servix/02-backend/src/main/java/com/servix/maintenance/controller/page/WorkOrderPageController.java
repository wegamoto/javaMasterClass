package com.servix.maintenance.controller.page;

import com.servix.maintenance.dto.WorkOrderDTO;
import com.servix.maintenance.model.WorkOrder;
import com.servix.maintenance.service.WorkOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/work-orders")
@RequiredArgsConstructor
public class WorkOrderPageController {

    private final WorkOrderService workOrderService;

    // ✅ หน้าเมนูหลักของ Work Orders
    @GetMapping
    public String workOrderMainPage() {
        return "workorders/workorders"; // templates/workorders/workorders.html
    }

    // ✅ แสดง list
    @GetMapping("/list")
    public String listPage(Model model) {
        model.addAttribute("workOrders", workOrderService.getAllWorkOrders());
        return "workorders/list"; // templates/workorders/list.html
    }

    // ✅ แสดงรายละเอียด
    @GetMapping("/{id}")
    public String detailPage(@PathVariable Long id, Model model) {
        WorkOrderDTO dto = workOrderService.getWorkOrderDtoById(id);
        model.addAttribute("workOrder", dto);
        return "workorders/detail"; // templates/workorders/detail.html
    }

    // ✅ หน้าแบบฟอร์มสำหรับสร้างใหม่
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("workOrder", new WorkOrderDTO());
        return "workorders/form"; // templates/workorders/form.html
    }

    // ✅ หน้าแบบฟอร์มสำหรับแก้ไข
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        WorkOrderDTO dto = workOrderService.getWorkOrderDtoById(id);
        model.addAttribute("workOrder", dto);
        return "workorders/form"; // templates/workorders/form.html
    }

    @PostMapping("/save")
    public String saveWorkOrder(@ModelAttribute("workOrder") WorkOrder workOrder) {
        // บันทึกข้อมูล
        workOrderService.save(workOrder);
        return "redirect:/work-orders/list";
    }

}
