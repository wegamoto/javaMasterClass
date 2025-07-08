package com.wewe.temjaisoft.dto.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class JournalEntryDto {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate postingDate;

    @NotBlank(message = "Reference No is required")
    private String referenceNo;

    private String description;

    @NotEmpty(message = "At least one ledger entry is required")
    private List<LedgerEntryDto> entries;
}
