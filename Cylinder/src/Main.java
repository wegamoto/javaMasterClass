//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Circle circle = new Circle(5);
        printCircle(circle);
    }

    public static void printCircle(Circle circle) {
        System.out.println("The area of the circle is " + circle.getArea());
        System.out.println("The radius of the circle is " + circle.getRadius());
    }
}