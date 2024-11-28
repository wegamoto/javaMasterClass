package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");
    }
    public class NumberToWords {

        public static int reverse(int number) {

            int reverse = 0;
            while (number != 0) {
                // multiply by 10 then and add last digit (number % 10)
                reverse = reverse * 10 + number % 10;
                number /= 10; // discard last digit
            }
            return reverse;
        }

        public static int getDigitCount(int number) {

            if (number < 0) {
                return -1;
            }

            if (number == 0) {
                return 1;
            }

            int count = 0;
            // start from 1 and multiply by 10 each step so i goes 1, 10, 100, 1000
            // count how many times it was multiplied, and that's the digit count
            for (int i = 1; i <= number; i *= 10) {
                count++;
            }
            return count;
        }

        public static void numberToWords(int number) {

            if (number < 0) {
                System.out.println("Invalid Value");
                return;
            }

            // get reversed number
            int reverse = reverse(number);

            // subtract numberDigits and reverseDigits to get leading zeroes
            int leadingZeroes = getDigitCount(number) - getDigitCount(reverse);

            if (number == 0) {
                System.out.println("Zero");
                return;
            }

            // use loop to print words
            while (reverse != 0) {
                int lastDigit = reverse % 10;
                switch (lastDigit) {
                    case 0 -> System.out.println("Zero");
                    case 1 -> System.out.println("One");
                    case 2 -> System.out.println("Two");
                    case 3 -> System.out.println("Three");
                    case 4 -> System.out.println("Four");
                    case 5 -> System.out.println("Five");
                    case 6 -> System.out.println("Six");
                    case 7 -> System.out.println("Seven");
                    case 8 -> System.out.println("Eight");
                    case 9 -> System.out.println("Nine");
                }
                reverse /= 10;
            }

            // print zeroes if there are any leading zeroes in reversed number
            for (int i = 0; i < leadingZeroes; i++) {
                System.out.println("Zero");
            }
        }
    }
}