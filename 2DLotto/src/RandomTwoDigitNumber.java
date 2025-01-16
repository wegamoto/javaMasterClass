import java.util.Random;

public class RandomTwoDigitNumber {
    public static void main(String[] args) {
        Random random = new Random();
        int randomNumber = random.nextInt(90) + 10;  // Generates a number between 10 and 99
        System.out.println("Random 2-digit number: " + randomNumber);
    }
}

