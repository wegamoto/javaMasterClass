package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.println(FirstLastDigitSum.sumFirstAndLastDigit(127));
        
    }
    public class FirstLastDigitSum {
        // write code here
        public static int sumFirstAndLastDigit(int number) {
            if (number < 0) {
                return -1;
            }
            int lastDigit = number % 10; // use % 10 to get last digit in a number.
            while (number >= 10) {
                number /= 10;
            }

            int firstDigit = number;
            return firstDigit + lastDigit;
        }
    }
}