package dev.lpa;

// ใช้ในการรับข้อมูลจากการสัมผัสพื้นผิวหรือแรงกด
public class TouchSensor {
    // Attributes for the touch sensor
    private boolean isPressed; // Indicates if the sensor is pressed

    // Constructor
    public TouchSensor() {
        this.isPressed = false; // Default state is not pressed
    }

    // Method to simulate pressing the sensor
    public void press() {
        isPressed = true;
        System.out.println("Touch sensor pressed.");
    }

    // Method to simulate releasing the sensor
    public void release() {
        isPressed = false;
        System.out.println("Touch sensor released.");
    }

    // Method to check if the sensor is pressed
    public boolean isPressed() {
        return isPressed;
    }

    // Method to get the state as a string
    public String getState() {
        return isPressed ? "Pressed" : "Not Pressed";
    }

    // Main method for testing
    public static void main(String[] args) {
        TouchSensor touchSensor = new TouchSensor();

        System.out.println("Initial State: " + touchSensor.getState());

        // Simulate pressing the sensor
        touchSensor.press();
        System.out.println("Current State: " + touchSensor.getState());

        // Simulate releasing the sensor
        touchSensor.release();
        System.out.println("Current State: " + touchSensor.getState());
    }
}
