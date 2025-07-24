package com.wewe.springlance.controller;

import com.wewe.springlance.model.User;
import com.wewe.springlance.model.enums.InvoiceStatus;
import com.wewe.springlance.repository.InvoiceRepository;
import com.wewe.springlance.repository.MessageRepository;
import com.wewe.springlance.repository.ProjectRequestRepository;
import com.wewe.springlance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
public class DashboardController {

    @Autowired private ProjectRequestRepository projectRequestRepository;
    @Autowired private MessageRepository messageRepository;
    @Autowired private InvoiceRepository invoiceRepository;
    @Autowired private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        // ดึง email (หรือ username) จาก user ที่ login อยู่
        String userEmail = principal.getName();
        Optional<User> currentUser = userRepository.findByEmail(userEmail);

        if (currentUser == null) {
            return "redirect:/login"; // หรือแสดง error
        }

        model.addAttribute("projectCount", projectRequestRepository.countByClient(currentUser));
        model.addAttribute("unreadMessages", messageRepository.countByReceiverAndIsReadFalse(currentUser));
        model.addAttribute(
                "pendingInvoices",
                invoiceRepository.countByFreelancerAndStatus(currentUser, InvoiceStatus.PENDING)
        );

        return "dashboard";
    }
}
