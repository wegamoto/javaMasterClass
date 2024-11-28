package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Dog rex = new Dog("rex");         // create instance (rex)
        Dog fluffy = new Dog("fluffy");   // create instance (fluffy)
        rex.printName();                        // print fluffy
        fluffy.printName();                     // print fluffy
    }
}