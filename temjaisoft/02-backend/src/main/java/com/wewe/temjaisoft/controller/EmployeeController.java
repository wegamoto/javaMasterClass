package com.wewe.temjaisoft.controller;

import com.wewe.temjaisoft.model.Department;
import com.wewe.temjaisoft.model.Employee;
import com.wewe.temjaisoft.model.Position;
import com.wewe.temjaisoft.repository.DepartmentRepository;
import com.wewe.temjaisoft.repository.PositionRepository;
import com.wewe.temjaisoft.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/hr")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeController(EmployeeService employeeService,
                              PositionRepository positionRepository,
                              DepartmentRepository departmentRepository) {
        this.employeeService = employeeService;
        this.positionRepository = positionRepository;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping({"", "/dashboard"})
    public String showHRDashboard(Model model, Principal principal) {
        model.addAttribute("username", principal != null ? principal.getName() : "Guest");
        return "hr/hr-dashboard"; // templates/hr/hr-dashboard.html (หน้า HR)
    }

//    // ✅ รองรับ /dashboard โดยตรง
//    @GetMapping("/dashboard")
//    public String showHRDashboardDirect() {
//        return "hr/hr-dashboard";
//    }

    @GetMapping("/employees/list")
    public String listEmployees(Model model, Principal principal) {
        model.addAttribute("username", principal !=null ? principal.getName() : "Guest");
        model.addAttribute("employees", employeeService.findAll());
        return "hr/employee-list"; // ✅ ตรงกับไฟล์ hr/employee-list.html
    }

    @GetMapping("/employees/new")
    public String showAddForm(Model model, Principal principal) {
        model.addAttribute("username", principal !=null ? principal.getName() : "Guest");
        setupFormModel(model, new Employee());
        return "hr/employee-form";
    }

    @PostMapping("/employees/save")
    public String saveEmployee(@ModelAttribute Employee employee, Model model, Principal principal) {
        model.addAttribute("username", principal !=null ? principal.getName() : "Guest");
        boolean invalid = employee.getPosition() == null || employee.getPosition().getId() == null
                || employee.getDepartment() == null || employee.getDepartment().getId() == null;

        if (invalid) {
            setupFormModel(model, employee);
            model.addAttribute("errorMessage", "กรุณาเลือกตำแหน่งและแผนกให้ครบถ้วน");
            return "hr/employee-form";
        }

        employee.setPosition(getPosition(employee.getPosition().getId()));
        employee.setDepartment(getDepartment(employee.getDepartment().getId()));

        employeeService.save(employee);
        return "redirect:/hr/employees";
    }

    @GetMapping("/employees/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, Principal principal) {
        model.addAttribute("username", principal !=null ? principal.getName() : "Guest");
        Employee employee = employeeService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบพนักงาน ID: " + id));
        setupFormModel(model, employee);
        return "hr/employee-form";
    }

    @PostMapping("/employees/update/{id}")
    public String updateEmployee(@PathVariable Long id, @ModelAttribute Employee employee) {
        employee.setId(id);
        employeeService.save(employee);
        return "redirect:/hr/employees/list";
    }

    @GetMapping("/employees/{id}")
    public String viewEmployeeDetails(@PathVariable Long id, Model model, Principal principal) {
        model.addAttribute("username", principal !=null ? principal.getName() : "Guest");
        Employee employee = employeeService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบพนักงาน ID: " + id));
        model.addAttribute("employee", employee);
        return "hr/employee-details";
    }

    @GetMapping("/employees/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, Principal principal) {
        employeeService.deleteById(id);
        return "redirect:/hr/employees";
    }

    // ⬇️ ส่วนจัดการตำแหน่ง ⬇️

    @GetMapping("/positions")
    public String listPositions(Model model, Principal principal) {
        model.addAttribute("positions", positionRepository.findAll());
        model.addAttribute("username", principal !=null ? principal.getName() : "Guest");
        return "hr/position-list";
    }

    @GetMapping("/positions/new")
    public String showAddPositionForm(Model model, Principal principal) {
        model.addAttribute("position", new Position());
        model.addAttribute("username", principal !=null ? principal.getName() : "Guest");
        return "hr/position-form";
    }

    @PostMapping("/positions")
    public String savePosition(@ModelAttribute Position position, Principal principal) {
        positionRepository.save(position);
        return "redirect:/hr/positions";
    }

    @GetMapping("/positions/edit/{id}")
    public String editPosition(@PathVariable Long id, Model model, Principal principal) {
        model.addAttribute("username", principal !=null ? principal.getName() : "Guest");
        Position position = getPosition(id);
        model.addAttribute("position", position);
        return "hr/position-form";
    }

    @GetMapping("/positions/delete/{id}")
    public String deletePosition(@PathVariable Long id, Principal principal) {
        positionRepository.deleteById(id);
        return "redirect:/hr/positions";
    }

    // ✅ Helper methods
    private void setupFormModel(Model model, Employee employee) {
        model.addAttribute("employee", employee);
        model.addAttribute("positions", positionRepository.findAll());
        model.addAttribute("departments", departmentRepository.findAll());
    }

    private Position getPosition(Long id) {
        return positionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบตำแหน่ง ID: " + id));
    }

    private Department getDepartment(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบแผนก ID: " + id));
    }
}
