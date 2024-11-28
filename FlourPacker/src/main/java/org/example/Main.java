package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Flour Paker " + FlourPacker.canPack(1,4,9));

    }
    public class FlourPacker {
        // write code here
        private static final int BIG_WEIGHT = 5;
        public static boolean canPack(int bigCount, int smallCount, int goal) {
            if (bigCount < 0 || smallCount < 0 || goal < 0) {
                return false;
            }

            boolean result = false;
            int totalBigWeight = bigCount * BIG_WEIGHT;

            if (totalBigWeight >= goal) {
                int remaining = goal % BIG_WEIGHT;
                if (smallCount >= remaining) {
                    return true;
                }
            } else {
                if (smallCount >= goal - totalBigWeight) {
                    return true;
                }
            }
            return result;
        }
    }
}