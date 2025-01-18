package dev.lpa;

public class Main {

    public static void main(String[] args) {

        String helloWorld = "%s %s".formatted("Hello", "World");
        String helloWorld2 = String.format("%s %s", "Hello", "World");
        System.out.println("Using string's formatted method: " + helloWorld);
        System.out.println("Using String.format: " + helloWorld2);

        String helloWorld3 = Main.format("%s %s", "Hello", "World");
        System.out.println("Using Main.format: " + helloWorld3);

        String testString = "Anyone can Learn abc's, 123's, and any regular expression";
        String replacement = "(-)";

        String[] patterns = {
                "[a-zA-Z]*$",
                "^[a-zA-Z]{3}",
                "[aA]ny\\b"
        };

        for (String pattern : patterns) {
            String output = testString.replaceFirst(pattern,replacement);
            System.out.println("Pattern: " + pattern + " => " + output);
        }
        // Song of the Witches in MacBeth, a Play by Shakespeare
        String paragraph = """
                Double, double toil and trouble;
                Fire burn and caldron bubble.
                Fillet of a fenny snake,
                In the caldron boil and bake
                Eye of newt and toe of frog,
                Wool of bat and tongue of dog,
                Adder's fork and blind-worm's string,
                Lizard's leg and howlet's wing,
                For a charm of powerful trouble,
                Like a hell-broth boil and bubble.
                """;
    }

    private static String format(String regexp, String... args) {

        int index =0;
        while (regexp.matches(".*%s.*")) {
            regexp =regexp.replaceFirst("%s",args[index++]);
        }
        return regexp;
    }

}
