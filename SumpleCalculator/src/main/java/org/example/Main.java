package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

    }
    public class SimpleCalculator {

        private double firstNumber;     // 0.0 by default
        private double secondNumber;    // 0.0 by default

        // == getters/setters ==
        public double getFirstNumber() {
            return firstNumber;
        }

        public void setFirstNumber(double firstNumber) {
            this.firstNumber = firstNumber;
        }

        public double getSecondNumber() {
            return secondNumber;
        }

        public void setSecondNumber(double secondNumber) {
            this.secondNumber = secondNumber;
        }

        // == public methods ==
        public double getAdditionResult() {
            return firstNumber + secondNumber;
        }

        public double getSubtractionResult() {
            return firstNumber - secondNumber;
        }

        public double getMultiplicationResult() {
            return firstNumber * secondNumber;
        }

        public double getDivisionResult() {
            // 1. check if we can divide with secondNumber
            if (secondNumber == 0) {
                return 0;
            }
            return firstNumber / secondNumber;
        }
    }
}