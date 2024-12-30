package dev.lpa;

public class LogisticCostCalculator {

    private static final double COST_PER_KM = 0.5; // Cost per kilometer in USD
    private static final double COST_PER_KG = 2.0; // Cost per kilogram in USD
    private static final double EXPRESS_SURCHARGE = 1.5; // Express delivery multiplier

    /**
     *
     * @param weight Weight of the package in kilograms
     * @param distance Distance of the delivery in kilometers
     * @param isExpress True if express delivery is required
     * @return The total logistic cost
     */

    public double calcuulateCost(double weight, double distance, boolean isExpress) {
        if (weight <= 0 || distance <= 0) {
            throw new IllegalArgumentException("Weight and distance must be positive values.");
        }

        double baseCost = (weight * COST_PER_KG) + (distance * COST_PER_KM);
        if (isExpress) {
            baseCost *= EXPRESS_SURCHARGE;
        }

        return baseCost;
    }
}
