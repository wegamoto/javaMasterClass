package com.wewe.springlance.controller;

import com.wewe.springlance.model.Message;
import com.wewe.springlance.model.ProjectRequest;
import com.wewe.springlance.model.User;
import com.wewe.springlance.service.MessageService;
import com.wewe.springlance.service.ProjectRequestService;
import com.wewe.springlance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ProjectRequestService projectRequestService;

    @Autowired
    private UserService userService;

    // แสดงหน้ารายการ project ที่ user มีข้อความ
    @GetMapping("")
    public String messageInbox(Principal principal, Model model) {
        User currentUser = userService.findByEmail(principal.getName()).orElse(null);
        if (currentUser == null) {
            return "redirect:/login";
        }

        List<ProjectRequest> projectsWithMessages = messageService.findProjectsByUser(currentUser);
        model.addAttribute("projects", projectsWithMessages);

        return "message/inbox";  // หน้า inbox ที่จะแสดง list โปรเจกต์
    }

    // แสดงหน้าข้อความในโปรเจกต์ที่เลือก
    @GetMapping("/project/{projectId}")
    public String viewMessages(@PathVariable Long projectId, Model model, Principal principal) {
        List<Message> messages = messageService.findByProjectId(projectId);
        ProjectRequest project = projectRequestService.findById(projectId).orElse(null);

        User currentUser = userService.findByEmail(principal.getName()).orElse(null);

        model.addAttribute("messages", messages);
        model.addAttribute("project", project);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("newMessage", new Message());

        return "message/chat"; // /templates/message/chat.html
    }

    // ส่งข้อความ
    @PostMapping("/send")
    public String sendMessage(@ModelAttribute Message message,
                              @RequestParam("projectId") Long projectId,
                              Principal principal) {

        User sender = userService.findByEmail(principal.getName()).orElse(null);
        ProjectRequest project = projectRequestService.findById(projectId).orElse(null);

        if (sender == null || project == null) {
            return "redirect:/error";
        }

        message.setSender(sender);
        message.setProject(project);
        message.setSentAt(LocalDateTime.now());
        message.setRead(false);

        if (project.getClient().getId().equals(sender.getId())) {
            message.setReceiver(project.getFreelancer());
        } else {
            message.setReceiver(project.getClient());
        }

        messageService.save(message);

        return "redirect:/messages/project/" + projectId;
    }
}
