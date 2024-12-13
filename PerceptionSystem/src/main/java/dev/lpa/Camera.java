package dev.lpa;

import java.util.ArrayList;

// ใช้ในการมองเห็นสิ่งแวดล้อม เช่น กล้อง RGB, กล้อง 3D
public class Camera {

    // Attributes for the Camera
    private boolean isOn; // Indicates if the camera is active
    private ArrayList<String> capturedImages; // Stores captured images as strings (image names or descriptions)

    // Constructor
    public Camera() {
        this.isOn = false; // Default state is off
        this.capturedImages = new ArrayList<>();
    }

    // Method to turn on the camera
    public void turnOn() {
        isOn = true;
        System.out.println("Camera is now ON.");
    }

    // Method to turn off the camera
    public void turnOff() {
        isOn = false;
        System.out.println("Camera is now OFF.");
    }

    // Method to check if the camera is on
    public boolean isOn() {
        return isOn;
    }

    // Method to capture an image
    public void captureImage(String imageName) {
        if (isOn) {
            capturedImages.add(imageName);
            System.out.println("Captured image: " + imageName);
        } else {
            System.out.println("Camera is OFF. Cannot capture image.");
        }
    }

    // Method to get all captured images
    public ArrayList<String> getCapturedImages() {
        return new ArrayList<>(capturedImages);
    }

    // Method to clear all captured images
    public void clearCapturedImages() {
        capturedImages.clear();
        System.out.println("All captured images have been cleared.");
    }

    // Main method for testing
    public static void main(String[] args) {
        Camera camera = new Camera();

        System.out.println("Camera status: " + (camera.isOn() ? "ON" : "OFF"));

        camera.captureImage("Image1.jpg"); // Should fail because camera is off

        camera.turnOn();
        camera.captureImage("Image1.jpg");
        camera.captureImage("Image2.jpg");

        System.out.println("Captured images: " + camera.getCapturedImages());

        camera.clearCapturedImages();
        System.out.println("Captured images after clearing: " + camera.getCapturedImages());

        camera.turnOff();
    }

}
