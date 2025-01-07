package dev.dto;

public class Transaction {

    private int routingNumber;

    private long transactionId;

    private int customerId;

    private double amount;

    public Transaction(int routingNumber, long transactionId, int customerId,
                       double amount) {
        this.routingNumber = routingNumber;
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.amount = amount;
    }

    public int getRoutingNumber() {
        return routingNumber;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "%15d:%20d:%015d:%012.2f".formatted(routingNumber, customerId,
                transactionId, amount);
    }
}
