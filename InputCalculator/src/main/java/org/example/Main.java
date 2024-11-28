package org.example;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        InputCalculator.inputThenPrintSumAndAverage();


    }
    public class InputCalculator {
        // write code here

        public static void inputThenPrintSumAndAverage() {
            Scanner scanner = new Scanner(System.in);

            int sum = 0;
            long avg = 0;
            int count = 0;

            while (true) {
                boolean hasNextInt = scanner.hasNextInt();
                if (!hasNextInt) {
                    break;
                }

                sum += scanner.nextInt();
                count++;
                avg = sum / count;
                scanner.nextLine();
            }
            if (count > 0) {
                avg = Math.round((double) sum / count);
            }
            System.out.println("SUM = " + sum + " AVG = " + avg );
            scanner.close();
        }
    }
}