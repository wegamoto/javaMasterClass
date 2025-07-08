package com.wewe.temjaisoft.repository.account;

import com.wewe.temjaisoft.model.account.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    List<JournalEntry> findByPostingDateBetween(LocalDate start, LocalDate end);
}

