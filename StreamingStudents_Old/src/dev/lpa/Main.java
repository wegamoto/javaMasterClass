package dev.lpa;

public class Main {

    public static void main(String[] args) {

        Course pymc = new Course("PYMC", "Python Masterclass");
        Course jmc = new Course("JMC", "Java Masterclass");
        Student tim = new Student("AU", 2019, 30, "M",
                true,jmc, pymc);
        System.out.println(tim);
    }
}
