package dev.lpa;

// ใช้สำหรับการรับรู้ตำแหน่งและระยะห่างของวัตถุรอบ ๆ
import java.util.HashMap;

public class RADAR {

    // Attributes for the RADAR system
    private boolean isActive; // Indicates if the RADAR is active
    private double range; // Maximum detection range in meters
    private HashMap<Double, Double> detectedObjects; // Detected objects with angle and distance

    // Constructor
    public RADAR(double range) {
        this.isActive = false; // Default state is inactive
        this.range = range;
        this.detectedObjects = new HashMap<>();
    }

    // Method to activate the RADAR
    public void activate() {
        isActive = true;
        System.out.println("RADAR is now ACTIVE.");
    }

    // Method to deactivate the RADAR
    public void deactivate() {
        isActive = false;
        System.out.println("RADAR is now INACTIVE.");
    }

    // Method to check if the RADAR is active
    public boolean isActive() {
        return isActive;
    }

    // Method to simulate object detection
    public void detectObject(double angle, double distance) {
        if (isActive) {
            if (distance <= range) {
                detectedObjects.put(angle, distance);
                System.out.printf("Object detected at angle %.2f°, distance: %.2f meters%n", angle, distance);
            } else {
                System.out.printf("Object at angle %.2f° is out of range (%.2f meters)%n", angle, distance);
            }
        } else {
            System.out.println("RADAR is INACTIVE. Cannot detect objects.");
        }
    }

    // Method to get all detected objects
    public HashMap<Double, Double> getDetectedObjects() {
        return new HashMap<>(detectedObjects);
    }

    // Method to clear all detected objects
    public void clearDetectedObjects() {
        detectedObjects.clear();
        System.out.println("All detected objects have been cleared.");
    }

    // Main method for testing
    public static void main(String[] args) {
        RADAR radar = new RADAR(100.0); // RADAR with 100-meter range

        System.out.println("RADAR status: " + (radar.isActive() ? "ACTIVE" : "INACTIVE"));

        radar.detectObject(45.0, 50.0); // Should fail because RADAR is inactive

        radar.activate();
        radar.detectObject(45.0, 50.0);
        radar.detectObject(90.0, 120.0);
        radar.detectObject(135.0, 80.0);

        System.out.println("Detected objects: " + radar.getDetectedObjects());

        radar.clearDetectedObjects();
        System.out.println("Detected objects after clearing: " + radar.getDetectedObjects());

        radar.deactivate();
    }
}

