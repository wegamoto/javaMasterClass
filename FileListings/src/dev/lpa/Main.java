package dev.lpa;

import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {

        Path path = Path.of("");
        System.out.println("cwd = " + path.toAbsolutePath());
    }
}
