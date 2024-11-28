package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Test Code
        Point point = new Point(3,4);
        point.distance();
        point.distance(10,10);
        point.distance(point);
        System.out.println(point.distance());
    }

}