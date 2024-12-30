package dev.lpa;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        LogisticCostCalculator calculator = new LogisticCostCalculator();

        // Example inputs
        double weight = 10.0; // in kilograms
        double distance = 50.0; // in kilometers
        boolean isExpress = true;

        double totalCost = calculator.calcuulateCost(weight, distance, isExpress);
        System.out.printf("The total logistic cost is: $%.2f%n", totalCost);
    }
}