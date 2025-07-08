package com.wewe.temjaisoft.service.account;

import com.wewe.temjaisoft.dto.account.JournalEntryDto;
import com.wewe.temjaisoft.dto.account.LedgerEntryDto;
import com.wewe.temjaisoft.model.account.Account;
import com.wewe.temjaisoft.model.account.JournalEntry;
import com.wewe.temjaisoft.model.account.LedgerEntry;
import com.wewe.temjaisoft.repository.account.AccountRepository;
import com.wewe.temjaisoft.repository.account.JournalEntryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;
    private final AccountRepository accountRepository;

    public JournalEntryService(JournalEntryRepository journalEntryRepository,
                               AccountRepository accountRepository) {
        this.journalEntryRepository = journalEntryRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public JournalEntry postJournalEntry(JournalEntryDto dto) {
        LocalDate postingDate = Optional.ofNullable(dto.getPostingDate()).orElse(LocalDate.now());

        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setPostingDate(postingDate);
        journalEntry.setReferenceNo(dto.getReferenceNo());
        journalEntry.setDescription(dto.getDescription());

        List<LedgerEntry> entries = buildLedgerEntries(dto.getEntries(), journalEntry, postingDate);
        validateBalanced(entries);

        journalEntry.setEntries(entries);
        return journalEntryRepository.save(journalEntry);
    }


    public List<JournalEntry> getAllEntries() {
        return journalEntryRepository.findAll();
    }

    // ✅ เพิ่มเมธอดนี้ใน Service class เดียวกัน
    private List<LedgerEntry> buildLedgerEntries(List<LedgerEntryDto> entryDtos, JournalEntry journalEntry, LocalDate postingDate) {
        List<LedgerEntry> entries = new ArrayList<>();

        for (LedgerEntryDto dto : entryDtos) {
            Account account = accountRepository.findById(dto.getAccountId())
                    .orElseThrow(() -> new IllegalArgumentException("Account not found for ID: " + dto.getAccountId()));

            LedgerEntry entry = new LedgerEntry();
            entry.setAccount(account);
            entry.setEntryDate(postingDate);
            entry.setDebit(dto.getDebit());
            entry.setCredit(dto.getCredit());
            entry.setDescription(dto.getDescription());
            entry.setJournalEntry(journalEntry);

            entries.add(entry);
        }

        return entries;
    }

    private void validateBalanced(List<LedgerEntry> entries) {
        BigDecimal totalDebit = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;

        for (LedgerEntry entry : entries) {
            totalDebit = totalDebit.add(entry.getDebit() != null ? entry.getDebit() : BigDecimal.ZERO);
            totalCredit = totalCredit.add(entry.getCredit() != null ? entry.getCredit() : BigDecimal.ZERO);
        }

        if (totalDebit.compareTo(totalCredit) != 0) {
            throw new IllegalArgumentException("Journal entry is not balanced. Total debit = " + totalDebit + ", total credit = " + totalCredit);
        }
    }
}

