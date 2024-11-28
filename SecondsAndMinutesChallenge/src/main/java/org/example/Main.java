package org.example;

import java.awt.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        System.out.println(getDurationString(-3945)); // This is the first
        System.out.println(getDurationString(-65, 45)); // This is the second test case
        System.out.println(getDurationString(65,145));
        System.out.println(getDurationString(65,45));
        System.out.println(getDurationString( 3945));
    }

    public static String getDurationString(int seconds) {
        // Two step approach to get hours
        if (seconds < 0) {
            return "Invalid data for seconds(" + seconds
                    + "), must be a positive integer value";
        }
            int minutes = seconds / 60;
            return getDurationString(seconds / 60, seconds % 60);
    }

    public static String getDurationString(int minutes, int seconds) {
        // One step approach to get hours
        if (minutes < 0) {
            return "Invalid data for minutes(" + minutes
                    + "), must be a positive integer value";
        }

        if (seconds < 0 || seconds > 59) {
            return  "Invalid data for seconds(" + seconds
                    + "), must be between 0 and 59";
        }

        int hours = minutes / 60;

        int remainingMinutes = minutes % 60;

        return hours + "h " + remainingMinutes + "m " + seconds + "s";
    }
}