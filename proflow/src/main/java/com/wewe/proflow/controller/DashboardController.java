package com.wewe.proflow.controller;

import com.wewe.proflow.model.Role;
import com.wewe.proflow.repository.CashFlowRepository;
import com.wewe.proflow.repository.ProjectRepository;
import com.wewe.proflow.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;

    private final CashFlowRepository cashFlowRepository;

    public DashboardController(ProjectRepository projectRepository, UserRepository userRepository, CashFlowRepository cashFlowRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.cashFlowRepository = cashFlowRepository;
    }

    @GetMapping
    public String dashboard(Model model) {
        // สถิติจาก DB
        long totalProjects = projectRepository.count();
        long totalContractors = userRepository.countByRole(Role.CONTRACTOR);

        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        double totalCashThisMonth = cashFlowRepository.sumAmountForCurrentMonth(startOfMonth, endOfMonth);
        double totalPendingPayments = cashFlowRepository.sumPendingPayments();

//        model.addAttribute("content", "dashboard/content :: content");

        // ส่งข้อมูลไปยัง fragment
        model.addAttribute("totalProjects", totalProjects);
        model.addAttribute("totalContractors", totalContractors);
        model.addAttribute("totalCashThisMonth", totalCashThisMonth);
        model.addAttribute("totalPendingPayments", totalPendingPayments);

        return "dashboard";
    }
}

