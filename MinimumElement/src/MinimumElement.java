import java.util.Scanner;

public class MinimumElement {
    // write code here

    private static int readInteger() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter count: ");
        int count = scanner.nextInt();

        return count;
    }

    private static int[] readElements(int count) {

        Scanner scanner = new Scanner(System.in);
        int[] array = new int[count];
        for (int i = 0; i < array.length; i++) {
            System.out.print("Enter a number: ");
            int number = scanner.nextInt();
            array[i] = number;
        }
        return array;
    }

    private static int findMin(int[] array) {

        int cmv = Integer.MAX_VALUE;
        for (int i = 0; i < array.length; i++) {
            if (array[i] < cmv) {
                cmv = array[i];
            }
        }
        return cmv;
    }
}