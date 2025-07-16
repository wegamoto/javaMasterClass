package com.wewe.promptinvoice.model;

public enum InvoiceStatus {
    PAID("ชำระแล้ว"),
    UNPAID("ค้างชำระ"),
    OVERDUE("เกินกำหนด");

    private final String displayName;

    InvoiceStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

