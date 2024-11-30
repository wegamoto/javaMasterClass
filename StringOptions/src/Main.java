//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        String helloWorld = "Hello" + " World";
        helloWorld.concat(" and Goodbye");

        StringBuilder hellowWorldBuilder = new StringBuilder("Hello" + " World");
        hellowWorldBuilder.append(" and Goodbye");

        printInformation(helloWorld);
        printInformation(hellowWorldBuilder);

        StringBuilder emptyStart = new StringBuilder();
        emptyStart.append("a".repeat(50));
        StringBuilder emptyStart32 = new StringBuilder(32);
        emptyStart32.append("a".repeat(17));

        printInformation(emptyStart);
        printInformation(emptyStart32);

        StringBuilder buildPlus = new StringBuilder("Hello" + " World");
        buildPlus.append(" and Goodbye");

        buildPlus.deleteCharAt(16).insert(16, 'g');
        System.out.println(buildPlus);

        buildPlus.replace(16, 17, "G");
        System.out.println(buildPlus);

        buildPlus.reverse().setLength(7);
        System.out.println(buildPlus);

    }

    public static void printInformation(String string) {
        System.out.println("String = " + string);
        System.out.println("length = " + string.length());
    }

    public static void printInformation(StringBuilder builder) {
        System.out.println("StringBuilder = " + builder);
        System.out.println("length = " + builder.length());
        System.out.println("capacity = " + builder.capacity());
    }
}