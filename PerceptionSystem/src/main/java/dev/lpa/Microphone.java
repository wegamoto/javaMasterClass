package dev.lpa;

import java.util.ArrayList;

// ใช้ในการรับเสียงและประมวลผลเสียง เช่น คำสั่งเสียง
public class Microphone {

    // Attributes for the Microphone
    private boolean isOn; // Indicates if the microphone is active
    private ArrayList<String> capturedAudio; // Stores captured audio as text

    // Constructor
    public Microphone() {
        this.isOn = false; // Default state is off
        this.capturedAudio = new ArrayList<>();
    }

    // Method to turn on the microphone
    public void turnOn() {
        isOn = true;
        System.out.println("Microphone is now ON.");
    }

    // Method to turn off the microphone
    public void turnOff() {
        isOn = false;
        System.out.println("Microphone is now OFF.");
    }

    // Method to check if the microphone is on
    public boolean isOn() {
        return isOn;
    }

    // Method to simulate capturing audio
    public void captureAudio(String audio) {
        if (isOn) {
            capturedAudio.add(audio);
            System.out.println("Captured audio: " + audio);
        } else {
            System.out.println("Microphone is OFF. Cannot capture audio.");
        }
    }

    // Method to get all captured audio
    public ArrayList<String> getCapturedAudio() {
        return new ArrayList<>(capturedAudio);
    }

    // Method to clear all captured audio
    public void clearCapturedAudio() {
        capturedAudio.clear();
        System.out.println("All captured audio has been cleared.");
    }

    // Main method for testing
    public static void main(String[] args) {
        Microphone mic = new Microphone();

        System.out.println("Microphone status: " + (mic.isOn() ? "ON" : "OFF"));

        mic.captureAudio("Hello, world!"); // Should fail because mic is off

        mic.turnOn();
        mic.captureAudio("Testing, 1, 2, 3.");
        mic.captureAudio("Another audio input.");

        System.out.println("Captured audio: " + mic.getCapturedAudio());

        mic.clearCapturedAudio();
        System.out.println("Captured audio after clearing: " + mic.getCapturedAudio());

        mic.turnOff();
    }
}
