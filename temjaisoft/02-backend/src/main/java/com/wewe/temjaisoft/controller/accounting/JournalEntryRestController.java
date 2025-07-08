package com.wewe.temjaisoft.controller.accounting;

import com.wewe.temjaisoft.dto.account.JournalEntryDto;
import com.wewe.temjaisoft.model.account.JournalEntry;
import com.wewe.temjaisoft.service.account.JournalEntryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/journals")
public class JournalEntryRestController {

    private final JournalEntryService journalEntryService;

    public JournalEntryRestController(JournalEntryService journalEntryService) {
        this.journalEntryService = journalEntryService;
    }

    @PostMapping
    public ResponseEntity<JournalEntry> postJournal(@RequestBody JournalEntryDto dto) {
        JournalEntry saved = journalEntryService.postJournalEntry(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<JournalEntry> getAll() {
        return journalEntryService.getAllEntries();
    }
}

