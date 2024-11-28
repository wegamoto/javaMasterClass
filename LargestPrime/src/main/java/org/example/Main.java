package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Largest Prime : " + LargestPrime.getLargestPrime(45));

    }
    public class LargestPrime {
        // write code here
        public static int getLargestPrime(int number) {
            if (number < 2) {
                return -1;
            }


            int factor = -1;
            for(int i = 2; i * i <= number; i++) {
                if (number % i !=0) {
                    continue;
                }

                factor = i;
                while (number % i == 0) {
                    number /= i;

                }
            }
            return number == 1 ? factor : number;
        }
    }
}