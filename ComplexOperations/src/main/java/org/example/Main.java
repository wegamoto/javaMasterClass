package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ComplexNumber complexNumber = new ComplexNumber(1, 2);
        complexNumber.add(1, 2);
        complexNumber.add(new ComplexNumber(1, 2));
        complexNumber.subtract(1, 2);
        complexNumber.subtract(new ComplexNumber(1, 2));
        System.out.println(complexNumber.getReal());
        System.out.println(complexNumber.getImaginary());

    }
}