package com.wewe.springlance.controller;

import com.wewe.springlance.model.Message;
import com.wewe.springlance.model.ProjectRequest;
import com.wewe.springlance.service.MessageService;
import com.wewe.springlance.service.ProjectRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ProjectRequestService projectRequestService;

    @GetMapping("/project/{projectId}")
    public String viewMessages(@PathVariable Long projectId, Model model) {
        List<Message> messages = messageService.findByProjectId(projectId);
        ProjectRequest project = projectRequestService.findById(projectId).orElse(null);
        model.addAttribute("messages", messages);
        model.addAttribute("project", project);
        model.addAttribute("newMessage", new Message());
        return "message/chat"; // /templates/message/chat.html
    }

    @PostMapping("/send")
    public String sendMessage(@ModelAttribute Message message) {
        message.setSentAt(LocalDateTime.now());
        messageService.save(message);
        return "redirect:/messages/project/" + message.getProject().getId();
    }
}
