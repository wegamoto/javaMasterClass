package org.example;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int counter = 1;
        double sum = 0;

        do {
            System.out.println("Enter number #" + counter + ":");
            String nextNumber = scanner.nextLine();
            try {
//                int number = Integer.parseInt(nextNumber);
                double number = Double.parseDouble(nextNumber);
                counter++;
                sum += number;
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid number");
            }
        } while (counter <= 5);
        System.out.println("The sum of the 5 number = "+ sum);
    }
}