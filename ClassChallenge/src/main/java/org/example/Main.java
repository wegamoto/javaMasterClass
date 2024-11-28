package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

//        Account bobsAccount = new Account("12345", 1000.00,
//                "Bob", "myemail@bob.com", "(087) 1234567");

        Account bobsAccount = new Account();



        System.out.println(bobsAccount.getNumber());
        System.out.println(bobsAccount.getBalance());
        System.out.println(bobsAccount.getCustomerName());
        System.out.println(bobsAccount.getCustomerEmail());
        System.out.println(bobsAccount.getCustomerPhone());

//        bobsAccount.setNumber("12345");
//        bobsAccount.setBalance(1000.00);
//        bobsAccount.setCustomerName("Bob Brown");
//        bobsAccount.setCustomerEmail("myemail@bob.com");
//        bobsAccount.setCustomerPhone("(087) 1234567");

        bobsAccount.withdrawFunds(100.0);
        bobsAccount.depositFunds(250);
        bobsAccount.withdrawFunds(50);

        bobsAccount.withdrawFunds(200);

        bobsAccount.depositFunds(100);
        bobsAccount.withdrawFunds(45.55);
        bobsAccount.withdrawFunds(54.46);

        bobsAccount.withdrawFunds(54.45);

        Account timAccount = new Account("Tim",
                "tim@email.com", "(066) 1234567");
        System.out.println("AccountNo: " + timAccount.getNumber() + "; name " + timAccount.getCustomerName());
    }
}