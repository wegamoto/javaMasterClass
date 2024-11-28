package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        int Output = EvenDigitSum.getEvenDigitSum(202050);
        System.out.printf("test " + Output);

    }
    public class EvenDigitSum {

        public static int getEvenDigitSum(int number) {

            if (number < 0) {
                return -1;
            }

            int sum = 0;
            for (int i = number; i > 0; i /= 10) {
                int lastDigit = i % 10;
                if (lastDigit % 2 == 0) {
                    sum += lastDigit;
                }
            }

            // == alternative using while loop ==
            // while (number > 0) {
            //     int lastDigit = number % 10;
            //     if (lastDigit % 2 == 0) {
            //         sum += lastDigit;
            //     }
            //     number /= 10;
            // }

            return sum;
        }
    }
}