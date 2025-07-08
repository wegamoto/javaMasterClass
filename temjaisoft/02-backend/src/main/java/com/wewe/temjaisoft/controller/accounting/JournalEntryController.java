package com.wewe.temjaisoft.controller.accounting;

import com.wewe.temjaisoft.dto.account.JournalEntryDto;
import com.wewe.temjaisoft.model.account.JournalEntry;
import com.wewe.temjaisoft.repository.account.AccountRepository;
import com.wewe.temjaisoft.service.account.JournalEntryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/journals")
public class JournalEntryController {

    private final JournalEntryService journalEntryService;
    private final AccountRepository accountRepository;

    public JournalEntryController(JournalEntryService journalEntryService, AccountRepository accountRepository) {
        this.journalEntryService = journalEntryService;
        this.accountRepository = accountRepository;
    }

    @GetMapping
    public String listJournals(Model model) {
        List<JournalEntry> journals = journalEntryService.getAllEntries();
        model.addAttribute("journals", journals);
        return "journal/list"; // -> resources/templates/journal/list.html
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("journalEntryDto", new JournalEntryDto());
        model.addAttribute("accounts", accountRepository.findAll());
        return "journal/form"; // -> resources/templates/journal/form.html
    }

    @PostMapping
    public String submitForm(@ModelAttribute JournalEntryDto dto, RedirectAttributes redirectAttributes) {
        journalEntryService.postJournalEntry(dto);
        redirectAttributes.addFlashAttribute("message", "บันทึกสำเร็จ!");
        return "redirect:/journals";
    }
}


