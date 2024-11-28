package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        DiagonalStar.printSquareStar(8);
        
    }
    public class DiagonalStar {
        // write code here
        private static final String STAR = "*";
        private static final String SPACE = " ";

        public static void printSquareStar(int number) {

            if (number < 5) {
                System.out.println("Invalid Value");
                return;
            }

            for (int row = 1; row <= number; row++) {
                for (int column = 1; column <= number; column++) {
                    boolean isFirstRowOrColumn = row == 1 || column == 1;
                    boolean isLastRowOrColumn = row ==number || column == number;
                    boolean isDiagonal = (row == column) || (column == (number - row + 1));

                    if (isFirstRowOrColumn || isLastRowOrColumn || isDiagonal) {
                        System.out.print(STAR);
                    } else {
                        System.out.print(SPACE);
                    }
                }
                System.out.println();
            }
        }
    }
}