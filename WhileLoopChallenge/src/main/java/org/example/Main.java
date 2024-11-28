package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        int number = 4;
        int finishNumber = 20;
        int evenCount = 0;
        int oddCount = 0;

        while (number <= finishNumber) {
            number++;
            if (!isEventNumber(number)) {
                oddCount++;
                continue;
            }
            System.out.println("Event number " + number);
            evenCount++;
        }

        System.out.println("Total odd numbers found = " + oddCount);
        System.out.println("Total even numbers found = " + evenCount);
    }

    public static boolean isEventNumber(int number) {
        if ((number % 2) == 0) {
            return true;
        } else {
            return false;
        }
    }
}