public class HourlyEmployee extends Employee{

    private double hourlyRate;

    public HourlyEmployee(String name, double hourlyRate, String birthDate, String hireDate,
                          double hourlyPayRate) {
        super(name, birthDate, hireDate);
        this.hourlyRate = hourlyRate;
    }

    @Override
    public double collectPay() {
        return hourlyRate * 40;
    }

    public double getDoubleHourlyRate() {
        return collectPay() * 2;
    }
}
