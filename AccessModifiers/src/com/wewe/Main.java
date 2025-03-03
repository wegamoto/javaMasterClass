package com.wewe;

public class Main {

    public static void main(String[] args) {
        Account weweAccount = new Account("Wewe");
        weweAccount.deposit(1000);
        weweAccount.withdraw(500);
        weweAccount.withdraw(-200);
        weweAccount.deposit(-20);
        weweAccount.calculateBalance();
        weweAccount.balance = 5000;  //Access Modifiers

        System.out.println("Balance on account is " + weweAccount.getBalance());
        weweAccount.transactions.add(4500);
        weweAccount.calculateBalance();
    }
}
