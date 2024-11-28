package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.println("Shared Digit = " + SharedDigit.hasSharedDigit(20,32));
        
    }
    public class SharedDigit {
        // write code here

        public static boolean hasSharedDigit(int first , int second ) {

            boolean firstValid = first > 9 && first < 100;
            boolean secondValid = second > 9 && second < 100;


            if (!firstValid || !secondValid) {
                return false;
            }

            int firstLeftDigit = first / 10;
            int firstRightDigit = first % 10;
            int secondLeftDigit = second / 10;
            int secondRightDigit = second % 10;

            boolean firstShared = firstLeftDigit == secondLeftDigit || firstLeftDigit == secondRightDigit ;
            boolean secondShared = firstRightDigit == secondLeftDigit || firstRightDigit == secondRightDigit;

            return firstShared || secondShared;
        }
    }
}