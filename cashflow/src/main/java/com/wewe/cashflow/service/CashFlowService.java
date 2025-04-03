package com.wewe.cashflow.service;

import com.wewe.cashflow.model.Transaction;
import com.wewe.cashflow.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CashFlowService {
    private final TransactionRepository transactionRepository;

    public CashFlowService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void recordIncome(double amount, String description) {
        transactionRepository.save(new Transaction("income", amount, description));
    }

    public void recordExpense(double amount, String description) {
        transactionRepository.save(new Transaction("expense", amount, description));
    }

    public double getBalance() {
        return transactionRepository.findAll().stream().mapToDouble(Transaction::getAmount).sum();
    }

    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }
}
