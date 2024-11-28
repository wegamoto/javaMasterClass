package org.example;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {

        int currentYear = 2022;

            System.out.println(getInputFromConsole(currentYear));
            System.out.println(getInputFromScanner(currentYear));
        try {
            System.out.println(getInputFromConsole(currentYear));
        } catch (NullPointerException e) {
            System.out.println(getInputFromScanner(currentYear));
        }
    }

    public static String getInputFromConsole(int currentYear) {

        String name = System.console().readLine("Hi, What's your Name? ");
        System.out.println("Hi " + name + ", Thanks for taking the course!");

        String dateOfBirth = System.console().readLine("What year were you born?" );
        int age = currentYear - Integer.parseInt(dateOfBirth);
        return "So you are " + age + " years old";
    }

    public static String getInputFromScanner(int currentYear) {
        Scanner scanner = new Scanner(System.in);
//        String name = System.console().readLine("Hi, What's your Name? ");
        System.out.println("What's your Name?");
        String name = scanner.nextLine();

        System.out.println("Hi " + ", Thanks for taking the course!");

//        String dateOfBirth = System.console().readLine("What year were you born?" );
        System.out.println("What year were you born?");

        boolean validDOB = false;
        int age = 0;

        do {
            System.out.println("Enter a year of birth >= " +
                    (currentYear - 125) + " and < = " + (currentYear));
            try {


                String dateOfBirth = scanner.nextLine();
                age = checkData(currentYear, scanner.nextLine());
                validDOB = age < 0 ? false : true;
            } catch(NumberFormatException badUserData) {
                System.out.println("Characters not allowed!!! Try again.");
            };
        } while (!validDOB);

        String dateOfBirth = scanner.nextLine();

        return "So you are " + age + " years old";
    }

    public static int checkData(int currentYear, String dateOfBirth) {
        int dob = Integer.parseInt(dateOfBirth);
        int minimumYear = currentYear - 125;

        if ((dob < minimumYear) || (dob > currentYear)) {
            return -1;
        }

        return (currentYear - dob);
    }
}