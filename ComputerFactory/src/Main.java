//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        ComputerCase theCase = new ComputerCase("2208", "Dell", "AC 100-240V");
        Monitor theMonitor = new Monitor("27inch Beast", "Acer", 27, "2540 x 1440");
        Motherboard theMotherboard = new Motherboard("BJ-200", "Asus",4,6,"v2.44");
        PersonalComputer thePC = new PersonalComputer("Gaming PC", "Lenovo", theCase, theMonitor, theMotherboard);


        thePC.powerUp();
    }
}