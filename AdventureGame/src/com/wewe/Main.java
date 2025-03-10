package com.wewe;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Map<Integer, Location> locations = new HashMap<Integer, Location>();

        locations.put(0, new Location(0, "You are sitting in front of a computer learning Java"));
        locations.put(1, new Location(1, "You are standing at the end of a road before a small brick building"));
        locations.put(2, new Location(2, "You are at the top of a hill"));
        locations.put(3, new Location(3, "You are inside a building, a well house for a small spring"));
        locations.put(4, new Location(4, "You are in a valley beside a stream"));
        locations.put(5, new Location(5, "You are in the forest"));

        locations.get(1).addExit("W", 2);
        locations.get(1).addExit("E", 3);
        locations.get(1).addExit("S", 4);
        locations.get(1).addExit("N", 5);

        locations.get(2).addExit("N", 5);

        locations.get(3).addExit("W", 1);

        locations.get(4).addExit("N", 1);
        locations.get(4).addExit("W", 2);

        locations.get(5).addExit("S", 1);
        locations.get(5).addExit("W", 2);

        // สร้างตัว Scanner สำหรับรับคำสั่งจากผู้เล่น
        Scanner scanner = new Scanner(System.in);
        String command;

        System.out.println("You are at the end of a road. Type your command (e.g., 'move N' or 'move S').");

        // ให้ผู้เล่นสามารถพิมพ์คำสั่งได้
        while (true) {
            System.out.print("Command: ");
            command = scanner.nextLine();

            // ถ้าผู้เล่นพิมพ์คำว่า "exit" จะออกจากเกม
            if (command.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the game.");
                break;
            }

            // ทดสอบการเคลื่อนที่
            ProcessBuilder location1 = null;
            location1.command(command);
        }

        scanner.close();
    }
}
