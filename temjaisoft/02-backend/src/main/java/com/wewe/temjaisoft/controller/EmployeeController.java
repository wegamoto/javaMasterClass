package com.wewe.temjaisoft.controller;

import com.wewe.temjaisoft.model.Employee;
import com.wewe.temjaisoft.model.Position;
import com.wewe.temjaisoft.repository.DepartmentRepository;
import com.wewe.temjaisoft.repository.EmployeeRepository;
import com.wewe.temjaisoft.repository.PositionRepository;
import com.wewe.temjaisoft.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Scanner;

@Controller
@RequestMapping("/hr")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeController(EmployeeRepository employeeRepository, PositionRepository positionRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.departmentRepository = departmentRepository;
    }

    // ✅ HR Dashboard (หน้า dashboard ย่อย)
    @GetMapping("")
    public String showHRDashboard() {
        return "hr/hr-dashboard"; // ไฟล์: hr/hr-dashboard.html
    }

    // ✅ แสดงรายการพนักงานทั้งหมด
    @GetMapping("/employees")
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        return "hr/employee-list";
    }

    // ✅ แสดงฟอร์มเพิ่มพนักงาน
    @GetMapping("/employees/new")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("positions", positionRepository.findAll());
        model.addAttribute("departments", departmentRepository.findAll());
        return "hr/employee-form";
    }

    // ✅ บันทึกข้อมูลพนักงานใหม่
    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute Employee employee) {
        employeeRepository.save(employee);
        return "redirect:/hr";
    }

    // ✅ แสดงฟอร์มแก้ไขพนักงาน
    @GetMapping("/employees/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Employee> employeeOpt = employeeRepository.findById(id);
        if (employeeOpt.isPresent()) {
            model.addAttribute("employee", employeeOpt.get());
            model.addAttribute("departments", departmentRepository.findAll());
            return "hr/employee-edit";
        } else {
            return "redirect:/hr";
        }
    }

    // ✅ อัปเดตข้อมูลพนักงาน
    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable Long id, @ModelAttribute Employee employee) {
        employee.setId(id);
        employeeRepository.save(employee);
        return "redirect:/hr";
    }

    // ✅ ดูรายละเอียดพนักงาน
    @GetMapping("/{id}")
    public String viewEmployeeDetails(@PathVariable Long id, Model model) {
        Optional<Employee> employeeOpt = employeeRepository.findById(id);
        if (employeeOpt.isPresent()) {
            model.addAttribute("employee", employeeOpt.get());
            return "hr/employee-details";
        } else {
            return "redirect:/hr";
        }
    }

    // ✅ ลบพนักงาน
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeRepository.deleteById(id);
        return "redirect:/hr";
    }

    @GetMapping("/positions")
    public String listPositions(Model model) {
        model.addAttribute("positions", positionRepository.findAll());
        return "hr/position-list";
    }

    @GetMapping("/positions/new")
    public String showAddPositionForm(Model model) {
        model.addAttribute("position", new Position());
        return "hr/position-form";
    }

    @PostMapping("/positions")
    public String savePosition(@ModelAttribute Position position) {
        positionRepository.save(position);
        return "redirect:/hr/positions";
    }

    @GetMapping("/positions/edit/{id}")
    public String editPosition(@PathVariable Long id, Model model) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid position ID: " + id));
        model.addAttribute("position", position);
        return "hr/position-form";
    }

    @GetMapping("/positions/delete/{id}")
    public String deletePosition(@PathVariable Long id) {
        positionRepository.deleteById(id);
        return "redirect:/hr/positions";
    }
}

