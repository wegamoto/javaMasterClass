package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Perfect Number : " + PerfectNumber.isPerfectNumber(6));

    }
    public class PerfectNumber {
        // write code here
        public static boolean isPerfectNumber(int number) {
            if (number < 1) {
                return false;
            }
            int sum = 0;
            for (int i = 1; i < number; i++) {
                if (number % i == 0) {
                    sum += i;
                }
            }
            return sum == number;
        }
    }
}