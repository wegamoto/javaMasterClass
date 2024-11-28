package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

    }
    public class Wall {

        private double width;
        private double height;

        public Wall() {}

        public Wall(double width, double height) {

            setWidth(width);
            setHeight(height);
        }

        public double getWidth() {
            return width;
        }

        public void setWidth(double width) {
            this.width = width > 0 ? width : 0;
        }

        public double getHeight() {
            return height;
        }

        public void setHeight(double height) {
            this.height = height > 0 ? height : 0;
        }

        public double getArea() {
            return width * height;
        }
    }
}