package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Result gcd : " + GreatestCommonDivisor.getGreatestCommonDivisor(25,50));

    }
    public class GreatestCommonDivisor {

        public static int getGreatestCommonDivisor(int first, int second) {

            if (first < 10 || second < 10) {
                return -1;
            }

            int min = first < second ? first : second;
            int gcd = 1;
            for (int j = 1; j <= min; j++) {
                if (first % j == 0 && second % j == 0) {
                    gcd = j;
                }
            }
            return gcd;
        }
    }
}