//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String bulletIt = "Print a Bulleted List:\n" +
                "\t\u2022 First Point\n" +
                "\t\t\u2022 Second Point\n" +
                "\t\t\t\u2022 Third Point";
        System.out.println(bulletIt);

        String textBlock = """
            Print a Bulleted List:
                    \u2022 First Point
                        \u2022 Sub Point """;
        System.out.println(textBlock);

        int age = 35;
        System.out.printf("My age is %d\n", age);

        int yearOfBirth = 2024 - age;
        System.out.printf("Age = %d, I was born in %d%n",age, yearOfBirth);

        System.out.printf("My age is %.2f%n", (float) age);

        for (int i = 1; i <= 10000; i *= 10) {
            System.out.printf("Printing %6d %n", i);
        }

        String formattedString = String.format("My age is %d", age);
        System.out.println(formattedString);

        formattedString = "My age is %d" .formatted(age);
        System.out.println(formattedString);
    }
}