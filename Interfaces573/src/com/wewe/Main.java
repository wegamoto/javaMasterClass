package com.wewe;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        ITelephone wewesPhone;
        wewesPhone = new DeskPhone(123456);
        wewesPhone.powerOn();
        wewesPhone.callPhone(123456);
        wewesPhone.answer();

        wewesPhone = new MobilePhone(24565);
        // wewesPhone.powerOn();
        wewesPhone.callPhone(24565);
        wewesPhone.answer();


    }
}