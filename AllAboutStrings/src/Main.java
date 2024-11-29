//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        printInformation("Hello World");
        printInformation("");
        printInformation("\t     \n");

        String helloWorld = "Hello World";
        System.out.printf("index of r = %d %n", helloWorld.indexOf('r'));
        System.out.printf("index of World = %d %n", helloWorld.indexOf("World", 6));

        System.out.printf("index of l = %d %n", helloWorld.indexOf('l'));
        System.out.printf("index of l = %d %n", helloWorld.lastIndexOf('l'));
        System.out.printf("index of l = %d %n", helloWorld.lastIndexOf('l',8));

        String helloWorldLower = helloWorld.toLowerCase();
        if (helloWorld.equals(helloWorldLower)) {
            System.out.println("The string is already in lower case");
        }
        if (helloWorld.equalsIgnoreCase(helloWorldLower)) {
            System.out.println("The string is already in lower case ignoring case");
        }

        if (helloWorld.startsWith("Hello")) {
            System.out.println("The string starts with Hello");
        }

        if (helloWorld.endsWith("World")) {
            System.out.println("The string ends with World");
        }

        if (helloWorld.contains("World")) {
            System.out.println("The string contains with World");
        }

        if (helloWorld.contentEquals("Hello World")) {
            System.out.println("The string values match exactly");
        }

    }

    public static void printInformation(String string) {
        int length = string.length();
        System.out.println("The length of the string is " + length);

        if (string.isEmpty()) {
            System.out.println("The string is empty");
            return;
        }
        if (string.isBlank()) {
            System.out.println("The string is blank");
            return;
        }
        System.out.println("The first letter of the string is " + string.charAt(0));
        System.out.println("The last letter of the string is = %c %n " + string.charAt(length - 1));
        System.out.println("The string is " + string);
    }
}