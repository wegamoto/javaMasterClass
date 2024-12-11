package dev.lpa;

import java.util.ArrayList;

public class Customer {
    private String name; // The customer's name
    private ArrayList<Double> transactions; // List to store transactions

    // Constructor
    public Customer(String name, double initialTransaction) {
        this.name = name;
        this.transactions = new ArrayList<>(); // Initializes transactions as an empty list
        addTransaction(initialTransaction); // Adds the initial transaction
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for transactions
    public ArrayList<Double> getTransactions() {
        return transactions;
    }

    // Add a transaction to the list
    public void addTransaction(double amount) {
        transactions.add(amount);
    }
}
