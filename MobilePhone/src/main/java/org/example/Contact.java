package org.example;

public class Contact {

    private String name;
    private String phoneNumber;

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public static Contact createContact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    };
}
