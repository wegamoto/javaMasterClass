package dev.lpa;

// ใช้สำหรับการรับรู้ตำแหน่งและระยะห่างของวัตถุรอบ ๆ
public class LIDAR {

    // Attributes for LIDAR sensor
    private double range; // Maximum range in meters
    private double resolution; // Angular resolution in degrees
    private double[] distances; // Detected distances in an array

    // Constructor
    public LIDAR(double range, double resolution) {
        this.range = range;
        this.resolution = resolution;
        this.distances = new double[(int) (360 / resolution)];
    }

    // Method to simulate scanning and populate distances
    public void scan() {
        for (int i = 0; i < distances.length; i++) {
            // Simulating distance detection (random values within range)
            distances[i] = Math.random() * range;
        }
    }

    // Method to get distances
    public double[] getDistances() {
        return distances;
    }

    // Method to get a specific angle's distance
    public double getDistanceAtAngle(double angle) {
        int index = (int) (angle / resolution);
        if (index >= 0 && index < distances.length) {
            return distances[index];
        } else {
            throw new IllegalArgumentException("Angle out of range");
        }
    }

    // Method to print all distances
    public void printDistances() {
        for (int i = 0; i < distances.length; i++) {
            double angle = i * resolution;
            System.out.printf("Angle: %.2f°, Distance: %.2f meters\n", angle, distances[i]);
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        LIDAR lidar = new LIDAR(10.0, 1.0); // 10-meter range, 1-degree resolution
        lidar.scan();
        lidar.printDistances();

        // Example: Get distance at specific angle
        try {
            double distanceAt90 = lidar.getDistanceAtAngle(90.0);
            System.out.printf("Distance at 90°: %.2f meters\n", distanceAt90);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

}
