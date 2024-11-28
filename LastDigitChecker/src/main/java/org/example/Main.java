package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        System.out.println("Result : " + LastDigitChecker.hasSameLastDigit(23,24,33));
    }
    public class LastDigitChecker {
        // write code here

        public static boolean isValid(int number) {
            return number >= 10 && number <= 1000;
        }

        public static boolean hasSameLastDigit(int num1, int num2, int num3) {


            if (!isValid(num1) || !isValid(num2) || !isValid(num3)) {
                return false;
            }

            int Lastnum1 = num1 % 10;
            int Lastnum2 = num2 % 10;
            int Lastnum3 = num3 % 10;

            return ( Lastnum1 == Lastnum2) || (Lastnum1 == Lastnum3) || (Lastnum2 == Lastnum3);
        }
    }
}