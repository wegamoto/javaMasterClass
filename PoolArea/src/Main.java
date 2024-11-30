//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
            Rectangle rectangle = new Rectangle(5, 10);
            System.out.println("Rectangle area: " + rectangle.getArea());
            System.out.println("Rectangle perimeter: " + rectangle.getLength() * 2 + rectangle.getWidth() * 2);
            System.out.println("Rectangle volume: " + rectangle.getArea() * rectangle.getLength());
            Cuboid cuboid = new Cuboid(5, 10, 15);
            System.out.println("Cuboid area: " + cuboid.getArea());
            System.out.println("Cuboid perimeter: " + cuboid.getLength() * 2 + cuboid.getWidth() * 2 + cuboid.getHeight() * 2);
            System.out.println("Cuboid volume: " + cuboid.getArea() * cuboid.getLength());
    }
}