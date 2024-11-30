public class PersonalComputer extends Product {

    private ComputerCase computerCase;
    private Monitor monitor;
    private Motherboard motherboard;

    public PersonalComputer(String model, String manufacturer) {
        super(model, manufacturer);
    }
    public void installWindows() {
        System.out.println("Installing Windows");
    }
    public void installLinux() {
        System.out.println("Installing Linux");
    }
    public void installMacOS() {
        System.out.println("Installing MacOS");
    }
    public void installWindows10() {
        System.out.println("Installing Windows 10");
    }
    public void installWindows8() {
        System.out.println("Installing Windows 8");
    }

    public PersonalComputer(String model, String manufacturer, ComputerCase computerCase, Monitor monitor, Motherboard motherboard) {
        super(model, manufacturer);
        this.computerCase = computerCase;
        this.monitor = monitor;
        this.motherboard = motherboard;
    }

    private void drawLogo() {
        monitor.drawPixel(1200, 50, "yellow");
    }

    public void powerUp() {

        computerCase.pressPowerButton();
        drawLogo();
    }
//    public ComputerCase getComputerCase() {
//        return computerCase;
//    }
//
//    public Monitor getMonitor() {
//        return monitor;
//    }
//
//    public Motherboard getMotherboard() {
//        return motherboard;
//    }
}
