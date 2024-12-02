public class Car {

    private String description;

    public Car(String description) {
        this.description = description;
    }

    public void startEngine() {
        System.out.println("Car -> Starting engine");
    }

    protected void runEngine() {
        System.out.println("Car -> Running engine");
    }

    public void drive() {
        System.out.println("Car -> Driving, type is " + getClass().getSimpleName());
        runEngine();
    }

}
class GasPoweredCar extends Car {
    private double avgKmPerLiter;
    private int cylinders = 6;

    private int batterySize;

    public GasPoweredCar(String description, double avgKmPerCharge) {
        super(description);

    }

    public GasPoweredCar(String description, double avgKmPerCharge, int cylinder) {
        super(description);
        this.avgKmPerLiter = avgKmPerCharge;
        this.cylinders = cylinder;
    }

    public GasPoweredCar(String description) {
        super(description);
    }

    @Override
    public void startEngine() {
        System.out.printf("Gas -> All %d cylinders are fired up, Ready!%n", cylinders);
    }
    @Override
    public void runEngine() {
        System.out.printf("Gas -> usage exceeds the average: %.2f %n", avgKmPerLiter);
    }
}

class ElectricCar extends Car {
    private double avgKmPerCharge;
    private int batterySize = 6;

    public ElectricCar(String description, double avgKmPerCharge) {
        super(description);

    }

    public ElectricCar(String description, double avgKmPerCharge, int cylinder) {
        super(description);
        this.avgKmPerCharge = avgKmPerCharge;
        this.batterySize = cylinder;
    }

    public ElectricCar(String description) {
        super(description);
    }

    @Override
    public void startEngine() {
        System.out.printf("BEV ->, switch %d kWh battery on, Ready!%n", batterySize);
    }
    @Override
    public void runEngine() {
        System.out.printf("BEV -> usage under the average: %.2f %n", avgKmPerCharge);
    }
}

class HybridCar extends Car {
    private double avgKmPerLiter;
    private int cylinders = 6;

    private int batterySize;

    public HybridCar(String description, double avgKmPerCharge) {
        super(description);

    }

    public HybridCar(String description, double avgKmPerCharge, int cylinder, int batterySize) {
        super(description);
        this.avgKmPerLiter = avgKmPerCharge;
        this.cylinders = cylinder;
        this.batterySize = batterySize;
    }

    public HybridCar(String description) {
        super(description);
    }

    @Override
    public void startEngine() {
        System.out.printf("Hybrid -> All %d cylinders are fired up, Ready!%n", cylinders);
        System.out.printf("Hybrid -> %d kWh battery on, Ready!%n", batterySize);
    }
    @Override
    public void runEngine() {
        System.out.printf("Hybrid -> usage below average: %.2f %n", avgKmPerLiter);
    }
}

