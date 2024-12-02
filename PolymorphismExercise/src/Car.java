public class Car {

    private boolean engine;
    private int cylinders;
    private String name;
    private int wheels;

    public Car(int cylinders, String name) {
        this.cylinders = cylinders;
        this.engine = true;
        this.name = name;
        this.wheels = 4;
    }

    public void setEngine(boolean engine) {
        this.engine = engine;
    }
    public boolean getEngine() {
        return engine;
    }
    public int getCylinders() {
        return cylinders;
    }
    public String getName() {
        return name;
    }

    public String startEngine() {
        return "Car -> startEngine()";
    }

    public String accelerate() {
        return "Car -> accelerate()";
    }

    public String brake() {
        return "Car -> brake()";
    }
}




