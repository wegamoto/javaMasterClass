package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        System.out.print(NumberPalindrome.isPalindrome(12344321));
    }
    public class NumberPalindrome {
        // write code here
        public static boolean isPalindrome(int number) {
            int reverse = 0;
            int original = number;

            while (original != 0) {
                reverse = reverse * 10 + original % 10;
                original /= 10;
            }

            return reverse == number;
        }
    }
}